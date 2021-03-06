package servlets.membre.pistes;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import musique.gestionnaires.GestionnaireMusiques;
import musique.modeles.Artiste;
import musique.modeles.Genre;
import musique.modeles.Musique;
import musique.modeles.Piste;
import utilisateurs.gestionnaires.GestionnaireUtilisateurs;

/**
 *
 * @author julien
 */
@WebServlet(name = "ModifierPiste", urlPatterns = {"/admin/pistes/modifier/*"})
public class Modifier extends HttpServlet {
    @EJB
    private GestionnaireMusiques gestionnaireMusiques;

    @EJB
    private GestionnaireUtilisateurs gestionnaireUtilisateurs;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String url = request.getRequestURL().toString();
        int idPiste = Integer.valueOf(url.substring(url.lastIndexOf("/") + 1));
        
        Piste piste = gestionnaireMusiques.getPiste(idPiste);
        
        request.setAttribute("listeDesMusiques", gestionnaireMusiques.getAllMusiques());
        request.setAttribute("piste", piste);
        this.getServletContext().getRequestDispatcher("/view/backoffice/pistes/modifier.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Musique musique = null;
        
        String inputMusique = request.getParameter("musique");
        String inputNom = request.getParameter("nom");
        String inputNote = request.getParameter("note");
        
        if(inputMusique != null && !inputMusique.isEmpty()) {
            musique = gestionnaireMusiques.getMusique(Integer.parseInt(inputMusique));
        }
        
        // Modification de l'utilisateur
        gestionnaireMusiques.modifierPiste(
                Integer.parseInt(request.getPathInfo().replaceAll("/", "")),
                musique,
                inputNom,
                Integer.parseInt(inputNote));

        // Redirection
        response.sendRedirect("/tp2webmiage/admin/pistes?etat=modifier");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
