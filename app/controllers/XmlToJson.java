package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.googlecode.xmlzen.XmlSlicer;
import play.libs.Json;

/**
 * Created by sylvain on 18/10/2014.
 */
public class XmlToJson {

    public static ObjectNode forJoueur(String xml) {

        final ObjectNode jsonFiche = Json.newObject();
        final XmlSlicer xmlFiche = XmlSlicer.cut(xml).getTag("liste").getTag("joueur");

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

        return jsonFiche;
    }

    public static ObjectNode forJoueurs(String xml) {

        final ObjectNode jsonFiche = Json.newObject();
        final ArrayNode joueurs = jsonFiche.putArray("joueurs");
        XmlSlicer.cut(xml).getTag("liste").getTags("joueur").forEach(
                xmlNode -> {
                    final ObjectNode joueur = Json.newObject();
                    joueur.put("licence", xmlNode.get("licence").toString());
                    joueur.put("nom", xmlNode.get("nom").toString());
                    joueur.put("prenom", xmlNode.get("prenom").toString());
                    joueur.put("club", xmlNode.get("club").toString());
                    joueur.put("nclub", xmlNode.get("nclub").toString());
                    joueur.put("clast", xmlNode.get("clast").toString());
                    joueurs.add(joueur);
                }
        );

        return jsonFiche;
    }

    public static ObjectNode forClub(String xml) {

        final ObjectNode jsonFiche = Json.newObject();
        final XmlSlicer xmlFiche = XmlSlicer.cut(xml).getTag("liste").getTag("club");

        jsonFiche.put("idclub", xmlFiche.get("idclub").toString());
        jsonFiche.put("numero", xmlFiche.get("numero").toString());
        jsonFiche.put("nom", xmlFiche.get("nom").toString());
        jsonFiche.put("nomsalle", xmlFiche.get("nomsalle").toString());
        jsonFiche.put("adressesalle1", xmlFiche.get("adressesalle1").toString());
        jsonFiche.put("adressesalle2", xmlFiche.get("adressesalle2").toString());
        jsonFiche.put("adressesalle3", xmlFiche.get("adressesalle3").toString());
        jsonFiche.put("codepsalle", xmlFiche.get("codepsalle").toString());
        jsonFiche.put("villesalle", xmlFiche.get("villesalle").toString());
        jsonFiche.put("web", xmlFiche.get("web").toString());
        jsonFiche.put("nomcor", xmlFiche.get("nomcor").toString());
        jsonFiche.put("prenomcor", xmlFiche.get("prenomcor").toString());
        jsonFiche.put("mailcor", xmlFiche.get("mailcor").toString());
        jsonFiche.put("telcor", xmlFiche.get("telcor").toString());
        jsonFiche.put("latitude", xmlFiche.get("latitude").toString());
        jsonFiche.put("longitude", xmlFiche.get("longitude").toString());

        return jsonFiche;
    }

    public static ObjectNode forClubs(String xml) {

        final ObjectNode json = Json.newObject();
        final ArrayNode clubs = json.putArray("clubs");
        XmlSlicer.cut(xml).getTag("liste").getTags("club").forEach(
                xmlNode -> {
                    final ObjectNode club = Json.newObject();
                    club.put("numero", xmlNode.get("numero").toString());
                    club.put("nom", xmlNode.get("nom").toString());
                    clubs.add(club);
                }
        );

        return json;
    }

}
