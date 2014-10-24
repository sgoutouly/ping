package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.cache.Cache;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;


/**
 * @author sylvain
 */
public class Application extends Controller {

    /**
     * @param licence
     * @return
     * @throws Exception
     */
    public static Promise<Result> joueur(String licence) throws Exception {
        Promise<JsonNode> joueur = Cache.getOrElse(
            "joueur_" + licence,
            () -> WS.url("http://www.fftt.com/mobile/xml/xml_joueur.php?licence=" + licence)
                    .get()
                    .filter(response -> response.getStatus() == Http.Status.OK)
                    .map(
                        response -> {
                            ObjectNode json = XmlToJson.forJoueur(response.getBody());
                            json.put("progmois", json.get("point").asInt() - json.get("apoint").asInt());
                            json.put("progann", json.get("point").asInt() - json.get("valinit").asInt());
                            return json;
                        }
                    ),
            60 * 60 * 1 // 1 heure en cache
        );
        return joueur.map(json -> handleEtag(json));
    }


    public static Promise<Result> equipes(String numClub) throws Exception {
        Promise<JsonNode> joueur = Cache.getOrElse(
                "equipes_" + numClub,
                () -> WS.url("http://www.fftt.com/mobile/xml/xml_equipe.php?numclu=" + numClub + "&type=M")
                        .get()
                        .filter(response -> response.getStatus() == Http.Status.OK)
                        .map(response -> XmlToJson.forEquipes(response.getBody()))
                ,
                60 * 60 * 24 * 30 // 30 jours en cache
        );
        return joueur.map(json -> handleEtag(json));
    }

    public static Promise<Result> classementEquipe(String division, String poule) throws Exception {
        Promise<JsonNode> joueur = Cache.getOrElse(
                "Classt_equipe_" + division + "_" + poule,
                () -> WS.url("http://www.fftt.com/mobile/xml/xml_result_equ.php?auto=1&action=classement&D1=" + division + "&cx_poule=" + poule)
                        .get()
                        .filter(response -> response.getStatus() == Http.Status.OK)
                        .map(response -> XmlToJson.forClassementEquipe(response.getBody()))
                ,
                60 * 60 * 24 // 1 jour en cache
        );
        return joueur.map(json -> handleEtag(json));
    }

    /**
     *
     * @param nom
     * @param prenom
     * @return
     * @throws Exception
     */
    public static Promise<Result> joueurs(String nom, String prenom) throws Exception {
        Promise<JsonNode> joueur = Cache.getOrElse(
                "joueurs_" + nom + "_" + prenom,
                () -> WS.url("http://www.fftt.com/mobile/xml/xml_liste_joueur.php?nom=" + nom + "&prenom=" + prenom )
                        .get()
                        .filter(response -> response.getStatus() == Http.Status.OK)
                        .map(response -> XmlToJson.forJoueurs(response.getBody()))
                ,
                60 * 60 * 1 // 1 heure en cache
        );
        return joueur.map(json -> handleEtag(json));
    }

    /**
     *
     * @param licence
     * @return
     * @throws Exception
     */
    public static Promise<Result> joueurParties(String licence) throws Exception {
        Promise<JsonNode> joueur = Cache.getOrElse(
                "joueurParties_" + licence,
                () -> WS.url("http://www.fftt.com/mobile/xml/xml_partie.php?numlic=" + licence)
                        .get()
                        .filter(response -> response.getStatus() == Http.Status.OK)
                        .map(response -> XmlToJson.forParties(response.getBody()))
                ,
                60 * 60 * 24 // 24 heure en cache
        );
        return joueur.map(json -> handleEtag(json));
    }

    /**
     * @param numero
     * @return
     * @throws Exception
     */
    public static Promise<Result> club(String numero) throws Exception {
        Promise<JsonNode> club = Cache.getOrElse(
            "club_" + numero,
            () -> WS.url("http://www.fftt.com/mobile/xml/xml_club_detail.php?club=" + numero)
                    .get()
                    .filter(response -> response.getStatus() == Http.Status.OK)
                    .map(response -> XmlToJson.forClub(response.getBody())
            ),
            60 * 60 * 24 // 24 heures en cache
        );
        return club.map(json -> handleEtag(json));
    }

    /**
     * @param numero
     * @return
     * @throws Exception
     */
    public static Promise<Result> clubs(String dept) throws Exception {
        Promise<JsonNode> club = Cache.getOrElse(
                "clubs_" + dept,
                () -> WS.url("http://www.fftt.com/mobile/xml/xml_club_dep2.php?dep=" + dept)
                        .get()
                        .filter(response -> response.getStatus() == Http.Status.OK)
                        .map(response -> XmlToJson.forClubs(response.getBody())
                        ),
                60 * 60 * 24 * 30 // 30 jours en cache
        );
        return club.map(json -> handleEtag(json));
    }

    /**
     * @param numero
     * @return
     * @throws Exception
     */
    public static Promise<Result> joueursByClub(String numero) throws Exception {
        final Promise<JsonNode> joueur = Cache.getOrElse(
            "joueursByClub_" + numero,
            () -> WS.url("http://www.fftt.com/mobile/xml/xml_liste_joueur.php?club=" + numero)
                    .get()
                    .filter(response -> response.getStatus() == Http.Status.OK)
                    .map(response -> XmlToJson.forJoueurs(response.getBody())),
            60 * 60 * 24 // 24 heures en cache
        );
        return joueur.map(json -> handleEtag(json));
    }

    /**
     * Effectue le traitement des ETAGs
     * @param json
     * @return Status
     */
    private static Status handleEtag(JsonNode json) {
        final String askedEtag = request().getHeader(Controller.IF_NONE_MATCH);
        final String currentEtag = json.get("etag").asText();
        if (askedEtag != null && askedEtag.equals(currentEtag)) {
            return status(NOT_MODIFIED);
        }
        else {
            response().setHeader(ETAG, currentEtag);
            return ok(json);
        }
    }


}
