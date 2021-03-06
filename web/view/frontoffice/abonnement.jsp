<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:frontoffice>
    <jsp:attribute name="breadcrumb">
        <div class="breadcrumb-wrapper">
            <span class="label">Vous êtes ici</span>
            <ol class="breadcrumb">
                <li><a href="index.jsp">Accueil</a></li>
                <li class="active">Prix</li>
            </ol>
        </div>
    </jsp:attribute>
    <jsp:body>
        <div class="page-header">
            <h3>Nos prix</h3>
        </div>
        <c:if test="${checkconnect == 'no'}">
            <div class="alert alert-danger"> Attention, vous devez être connecté pour vous abonner</div>
        </c:if>
        <div class="pricing pricing3 clearfix">
            <div class="pricing-wrap">
                <div class="pricing-inner">
                    <div class="pricing-title"><h4>Abonnement "basique"<span>Pour les amateurs</span></h4></div>
                    <div class="pricing-price">3<span class="price-sub">99</span>€<span class="price-tenure">par mois</span></div>
                    <div class="pricing-features">
                        <ul>
                            <li><span>3</span> Musiques</li>
                            <li><span>Toutes les pistes</span> de ces musiques</li>
                            <li>Téléchargement <span>illimité</span> de ces musiques</li>
                            <li>Support <span>Email</span> uniquement</li>
                            <li>&nbsp;</li>
                        </ul>
                    </div>
                    <c:if test="${checkconnect == 'yes'}">
                        <form action = "" method = "post">
                            <input type="hidden" name="type_abo" value="BASIC">
                            <div class="pricing-action">
                                <input type="submit" class="btn btn-primary" value="S'abonner">
                            </div>
                        </form>
                    </c:if>
                </div>
            </div>
            <div class="pricing-wrap best-price">
                <div class="pricing-inner">
                    <div class="pricing-title"><h4>Abonnement "limité"<span>Notre meilleur abonnement</span></h4></div>
                    <div class="pricing-price">14<span class="price-sub">99</span>€<span class="price-tenure">par mois</span></div>
                    <div class="pricing-features">
                        <ul>
                            <li><span>50</span> musiques par mois</li>
                            <li><span>Toutes les pistes</span> de ces musiques</li>
                            <li>Téléchargement <span>illimité</span> de ces musiques</li>
                            <li><span>Sans</span> engagement</li>
                            <li>Support <span>Téléphone & Email</span></li>
                        </ul>
                    </div>
                    <c:if test="${checkconnect == 'yes'}">
                        <form action = "" method = "post">
                            <input type="hidden" name="type_abo" value="LIMITED">
                            <div class="pricing-action">
                                <input type="submit" class="btn btn-primary" value="S'abonner">
                            </div>
                        </form>
                    </c:if>
                </div>
            </div>
            <div class="pricing-wrap">
                <div class="pricing-inner">
                    <div class="pricing-title"><h4>Abonnement "illimité"<span>Pour les professionnels</span></h4></div>
                    <div class="pricing-price">24<span class="price-sub">99</span>€<span class="price-tenure">par mois</span></div>
                    <div class="pricing-features">
                        <ul>
                            <li>Téléchargement <span>illimité</span> des musiques</li>
                            <li><span>Toutes les pistes</span> de ces musiques</li>
                            <li>Téléchargement <span>illimité</span> de ces musiques</li>
                            <li><span>Sans</span> engagement</li>
                            <li>Support <span>Téléphone & Email</span></li>
                        </ul>
                    </div>
                    <c:if test="${checkconnect == 'yes'}">
                        <form action = "" method = "post">
                            <input type="hidden" name="type_abo" value="UNLIMITED">
                            <div class="pricing-action">
                                <input type="submit" class="btn btn-primary" value="S'abonner">
                            </div>
                        </form>
                    </c:if>
                </div>
            </div>
        </div>

        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Ã—</button>
                        <h4 class="modal-title" id="myModalLabel">Achat d'un titre</h4>
                    </div>
                    <div class="modal-body">
                        <form action="https://www.sandbox.paypal.com/cgi-bin/webscr" method="post">
                            <input type='hidden' value="${panier.getMusiques().size() * 3.99}" name="amount" />
                            <input name="currency_code" type="hidden" value="EUR" />
                            <input name="return" type="hidden" value="http://localhost:8080/tp2webmiage/musiques" />
                            <input name="cancel_return" type="hidden" value="http://localhost:8080/tp2webmiage/musiques" />
                            <input name="notify_url" type="hidden" value="http://votredomaine/validationPaiement.php" />
                            <input name="cmd" type="hidden" value="_xclick" />
                            <input name="business" type="hidden" value="julienblacas@gmail.com" />
                            <input name="item_name" type="hidden" value="Panier MusicComposer" />
                            <input name="no_note" type="hidden" value="${panier.getMusiques().size()}" />
                            <input name="lc" type="hidden" value="FR" />
                            <input name="bn" type="hidden" value="PP-BuyNowBF" />
                            <input name="custom" type="hidden" value="ID_ACHETEUR" />
                            <input name="submit" class="btn btn-primary" type="image" value="Payer avec paypal"/>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <form action = "" method = "post">
                            <input type="hidden" name="action" value="buy">
                            <input type="submit" class="btn btn-success pull-left" data-toggle="modal" data-target="#myModal" value="Acheter">
                        </form>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div><!-- modal-content -->
            </div><!-- modal-dialog -->
        </div>
    </jsp:body>
</t:frontoffice>