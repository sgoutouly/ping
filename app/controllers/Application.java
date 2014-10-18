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
        return Cache.getOrElse(
            "joueur_" + licence,
            () -> WS.url("http://www.fftt.com/mobile/xml/xml_joueur.php?licence=" + licence).get().map(
                response -> ok(XmlToJson.forJoueur(response.getBody()))
            ),
            60 * 60 * 1 // 1 heure en cache
        );
    }

    /**
     * @param numero
     * @return
     * @throws Exception
     */
    public static Promise<Result> club(String numero) throws Exception {
        return Cache.getOrElse(
                "club_" + numero,
                () -> WS.url("http://www.fftt.com/mobile/xml/xml_club_detail.php?club=" + numero).get().map(
                    response -> ok(XmlToJson.forClub(response.getBody()))
                ),
                60 * 60 * 24 // 24 heures en cache
        );
    }

    /**
     * @param numero
     * @return
     * @throws Exception
     */
    public static Promise<Result> joueursByClub(String numero) throws Exception {
        return Cache.getOrElse(
                "club_" + numero,
                () -> WS.url("http://www.fftt.com/mobile/xml/xml_liste_joueur.php?club=" + numero).get().map(
                        response -> ok(XmlToJson.forJoueurs(response.getBody()))
                ),
                60 * 60 * 24 // 24 heures en cache
        );
    }

}
