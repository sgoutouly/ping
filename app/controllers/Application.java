package controllers;

import play.cache.Cache;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.ws.WS;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.googlecode.xmlzen.XmlSlicer;
import utils.ETagHelper;

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
            () -> WS.url("http://www.fftt.com/mobile/xml/xml_joueur.php?licence=" + licence).get().map(
                response -> XmlToJson.forJoueur(response.getBody())
            ),
            60 * 60 * 1 // 1 heure en cache
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
                () -> WS.url("http://www.fftt.com/mobile/xml/xml_club_detail.php?club=" + numero).get().map(
                    response -> XmlToJson.forClub(response.getBody())
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
    public static Promise<Result> joueursByClub(String numero) throws Exception {
        final Promise<JsonNode> joueur = Cache.getOrElse(
                "club_" + numero,
                () -> WS.url("http://www.fftt.com/mobile/xml/xml_liste_joueur.php?club=" + numero).get().map(
                        response -> XmlToJson.forJoueurs(response.getBody())
                ),
                60 * 60 * 24 // 24 heures en cache
        );
        return joueur.map(json -> handleEtag(json));
    }


    private static Status handleEtag(JsonNode json) {
        final String askedEtag = request().getHeader(Controller.IF_NONE_MATCH);
        if (askedEtag.equals(ETagHelper.getMd5Digest(json.toString().getBytes()))) {
            return status(NOT_MODIFIED);
        }
        else {
            final String newEtag = ETagHelper.getMd5Digest(json.toString().getBytes());
            response().setHeader(ETAG, newEtag);
            return ok(json);
        }
    }

}
