package servlets.visiteur;

import abonnement.gestionnaire.GestionnaireAbonnements;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import musique.gestionnaires.GestionnaireMusiques;
import musique.modeles.Artiste;
import musique.modeles.Musique;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import panier.gestionnaire.GestionnairePanier;
import utilisateurs.gestionnaires.GestionnaireUtilisateurs;
import utilisateurs.modeles.Adresse;

/**
 *
 * @author Guillaume
 */
@WebFilter(filterName = "/", urlPatterns = {"/*", "/"}, dispatcherTypes = {DispatcherType.REQUEST})
public class AccueilFilter implements Filter {
    
    GestionnaireAbonnements gestionnaireAbonnements = lookupGestionnaireAbonnementsBean();
    GestionnaireUtilisateurs gestionnaireUtilisateurs = lookupGestionnaireUtilisateursBean();
    GestionnaireMusiques gestionnaireMusiques = lookupGestionnaireMusiquesBean();

    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public AccueilFilter() {
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        // Test si un panier est déclaré
        HttpServletRequest httprequest = (HttpServletRequest) request;
        HttpSession session = httprequest.getSession();
        if (session.getAttribute("panier") == null) {
            session.setAttribute("panier", new GestionnairePanier());
        }

        request.setAttribute("panier", session.getAttribute("panier"));
        
            // Il n'y a pas d'abonnements
        if (gestionnaireAbonnements.getAbonnements().isEmpty()) {
            gestionnaireAbonnements.creerAbonnement();
        }
        // Il n'y a pas d'utilisateurs
        if (gestionnaireUtilisateurs.getAllUsers().isEmpty()) {
            Adresse nice = new Adresse("NICE", "06480");
            gestionnaireUtilisateurs.creeUtilisateur("Administrateur", "", "admin", "admin", "admin@musiccomposer.fr", nice, "0493320233");
        }

        //Test si les musiques sont vides
        if (gestionnaireMusiques.getAllMusiques().isEmpty()) {
            JSONParser parser = new JSONParser();
            String chaine = "";
            try {
                InputStream ips = new FileInputStream(request.getServletContext().getRealPath("") + "\\ressources\\musique.json");
                InputStreamReader ipsr = new InputStreamReader(ips);
                try (BufferedReader br = new BufferedReader(ipsr)) {
                    String ligne;
                    while ((ligne = br.readLine()) != null) {
                        chaine += ligne + "\n";
                    }
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }

            try {
                Object obj = parser.parse(chaine);
                JSONArray array = (JSONArray) obj;
                for (int i = 0; i < array.size(); i++) {
                    JSONObject objet = (JSONObject) array.get(i);
                    JSONArray compositions = (JSONArray) objet.get("composition");
                    String nom = (String) objet.get("nom");

                    if (nom.indexOf("-") != 0) {
                        Artiste artiste = null;
                        String nomArtiste = nom.substring(0, nom.indexOf("-") - 1);
                        artiste = gestionnaireMusiques.getArtiste(nomArtiste);
                        if (gestionnaireMusiques.getArtiste(nomArtiste) == null) {
                            artiste = gestionnaireMusiques.creerArtiste(nomArtiste, "", "");
                        }
                        
                        nom = nom.substring(nom.indexOf("-") + 1);
                        Musique musique = gestionnaireMusiques.creerMusique(artiste, nom, getNombrePiste(compositions, null), 0, "", null);
                        getNombrePiste(compositions, musique);
                    }
                }
            } catch (ParseException pe) {
                System.out.println("position: " + pe.getPosition());
                System.out.println(pe);
            }
        }

        chain.doFilter(request, response);
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     *
     * @param compositions Tableau de pistes
     * @param musique Si une musique est donnée cela crée la piste
     * @return Un nombre de piste
     */
    public int getNombrePiste(JSONArray compositions, Musique musique) {
        int nbpiste = 0;
        for (int j = 0; j < compositions.size(); j++) {
            String piste = compositions.get(j).toString();
            String extension = piste.substring(piste.lastIndexOf('.') + 1).toLowerCase();
            if (piste.lastIndexOf('.') != -1 && (extension.compareTo("mp3") == 0 || extension.compareTo("ogg") == 0)) {
                if (musique != null) {
                    gestionnaireMusiques.creerPiste(musique, (String) compositions.get(j), 0);
                }

                nbpiste++;
            }
        }
        return nbpiste;
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    private GestionnaireMusiques lookupGestionnaireMusiquesBean() {
        try {
            Context c = new InitialContext();
            return (GestionnaireMusiques) c.lookup("java:global/tp2webmiage/GestionnaireMusiques!musique.gestionnaires.GestionnaireMusiques");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private GestionnaireUtilisateurs lookupGestionnaireUtilisateursBean() {
        try {
            Context c = new InitialContext();
            return (GestionnaireUtilisateurs) c.lookup("java:global/tp2webmiage/GestionnaireUtilisateurs!utilisateurs.gestionnaires.GestionnaireUtilisateurs");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private GestionnaireAbonnements lookupGestionnaireAbonnementsBean() {
        try {
            Context c = new InitialContext();
            return (GestionnaireAbonnements) c.lookup("java:global/tp2webmiage/GestionnaireAbonnements!abonnement.gestionnaire.GestionnaireAbonnements");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
