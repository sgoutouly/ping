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
                response -> {
                    ObjectNode jsonFiche = Json.newObject();
                    XmlSlicer xmlFiche = XmlSlicer.cut(response.getBody()).getTag("liste").getTag("joueur");

                    jsonFiche.put("licence", xmlFiche.get("licence").toString());
                    jsonFiche.put("nom", xmlFiche.get("nom").toString());
                    jsonFiche.put("prenom", xmlFiche.get("prenom").toString());
                    jsonFiche.put("club", xmlFiche.get("club").toString());
                    jsonFiche.put("nclub", xmlFiche.get("nclub").toString());
                    jsonFiche.put("natio", xmlFiche.get("natio").toString());
                    jsonFiche.put("clglob", xmlFiche.get("clglob").toString());
                    jsonFiche.put("point", xmlFiche.get("point").toString());
                    jsonFiche.put("aclglob", xmlFiche.get("aclglob").toString());
                    jsonFiche.put("apoint", xmlFiche.get("apoint").toString());
                    jsonFiche.put("clast", xmlFiche.get("clast").toString());
                    jsonFiche.put("clnat", xmlFiche.get("clnat").toString());
                    jsonFiche.put("categ", xmlFiche.get("categ").toString());
                    jsonFiche.put("rangreg", xmlFiche.get("rangreg").toString());
                    jsonFiche.put("rangdep", xmlFiche.get("rangdep").toString());
                    jsonFiche.put("valcla", xmlFiche.get("valcla").toString());
                    jsonFiche.put("clpro", xmlFiche.get("clpro").toString());
                    jsonFiche.put("valinit", xmlFiche.get("valinit").toString());

                    return ok(jsonFiche);
                }
            ),
            60 * 60 * 1
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
                    response -> {
                        ObjectNode jsonFiche = Json.newObject();
                        XmlSlicer xmlFiche = XmlSlicer.cut(response.getBody()).getTag("club");
                        jsonFiche.put("licence", xmlFiche.get("licence").toString());
                        jsonFiche.put("nom", xmlFiche.get("nom").toString());
                        jsonFiche.put("prenom", xmlFiche.get("prenom").toString());
                        jsonFiche.put("club", xmlFiche.get("club").toString());
                        jsonFiche.put("nclub", xmlFiche.get("nclub").toString());
                        jsonFiche.put("natio", xmlFiche.get("natio").toString());
                        jsonFiche.put("clglob", xmlFiche.get("clglob").toString());
                        jsonFiche.put("point", xmlFiche.get("point").toString());
                        jsonFiche.put("aclglob", xmlFiche.get("aclglob").toString());
                        jsonFiche.put("apoint", xmlFiche.get("apoint").toString());
                        jsonFiche.put("clast", xmlFiche.get("clast").toString());
                        jsonFiche.put("clnat", xmlFiche.get("clnat").toString());
                        jsonFiche.put("categ", xmlFiche.get("categ").toString());
                        jsonFiche.put("rangreg", xmlFiche.get("rangreg").toString());
                        jsonFiche.put("rangdep", xmlFiche.get("rangdep").toString());
                        jsonFiche.put("valcla", xmlFiche.get("valcla").toString());
                        jsonFiche.put("clpro", xmlFiche.get("clpro").toString());
                        jsonFiche.put("valinit", xmlFiche.get("valinit").toString());
                        return ok(jsonFiche);
                    }
                ),
                60 * 60 * 24
        );
    }

}
