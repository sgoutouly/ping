package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.cache.Cache;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.mvc.Controller;
import play.mvc.Result;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
     *
     * @param nom
     * @param prenom
     * @return
     * @throws Exception
     */
    public static Promise<Result> joueurs(String nom, String prenom) throws Exception {
        Promise<JsonNode> joueur = Cache.getOrElse(
                "joueurs_" + nom + "_" + prenom,
                () -> WS.url("http://www.fftt.com/mobile/xml/xml_liste_joueur.php?nom=" + nom + "&prenom=" + prenom ).get().map(
                        response -> XmlToJson.forJoueurs(response.getBody())
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

    /**
     * Effectue le traitement des ETAGs
     * @param json
     * @return Status
     */
    private static Status handleEtag(JsonNode json) {
        final String askedEtag = request().getHeader(Controller.IF_NONE_MATCH);
        if (askedEtag != null && askedEtag.equals(md5Digest(json.toString()))) {
            return status(NOT_MODIFIED);
        }
        else {
            response().setHeader(ETAG, md5Digest(json.toString()));
            return ok(json);
        }
    }

    /**
     * md5Digest
     *
     * @param data
     * @return String
     */
    public static String md5Digest(String data) {
        try {
            byte[] bytes = data.getBytes();
            final MessageDigest md = MessageDigest.getInstance("MD5");
            final byte[] messageDigest = md.digest(bytes);
            final BigInteger number = new BigInteger(1, messageDigest);
            // prepend a zero to get a "proper" MD5 hash value
            final StringBuffer sb = new StringBuffer('0');
            sb.append(number.toString(16));
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 cryptographic algorithm is not available.", e);
        }

    }

}
