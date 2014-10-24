package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.googlecode.xmlzen.XmlSlicer;
import org.apache.commons.lang3.StringUtils;
import play.libs.Json;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

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
        jsonFiche.put("etag", md5Digest(jsonFiche.toString()));
        return jsonFiche;
    }

    public static ObjectNode forEquipes(String xml) {

        final ObjectNode json = Json.newObject();
        final ArrayNode equipes = json.putArray("equipes");
        XmlSlicer.cut(xml).getTag("liste").getTags("equipe").forEach(
                xmlNode -> {
                    final ObjectNode equipe = Json.newObject();
                    equipe.put("libequipe", xmlNode.get("libequipe").toString());
                    equipe.put("libdivision", xmlNode.get("libdivision").toString());
                    final String[] tmp = xmlNode.get("liendivision").toString().split("&amp;");
                    final String poule = tmp[0];
                    final String division = tmp[1];
                    equipe.put("cx_poule", StringUtils.substringAfter(tmp[0], "="));
                    equipe.put("D1", StringUtils.substringAfter(tmp[1], "="));
                    equipe.put("organisme_pere", StringUtils.substringAfter(tmp[2], "="));

                    // Hypermédia avec HAL : lien relatif pour simplifier
                    final ObjectNode link = Json.newObject();
                    link.put("href", "/classements/equipe?" + division + "&" + poule);
                    final ObjectNode links = Json.newObject();
                    links.put("classement", link);
                    equipe.put("_links", links);

                    equipes.add(equipe);
                }
        );
        json.put("etag", md5Digest(json.toString()));
        return json;
    }

    public static ObjectNode forClassementEquipe(String xml) {

        final ObjectNode json = Json.newObject();
        final ArrayNode classements = json.putArray("classement");
        XmlSlicer.cut(xml).getTag("liste").getTags("classement").forEach(
                xmlNode -> {
                    final ObjectNode classement = Json.newObject();
                    classement.put("poule", xmlNode.get("poule").toString());
                    classement.put("clt", xmlNode.get("clt").toString());
                    classement.put("equipe", xmlNode.get("equipe").toString());
                    classement.put("joue", xmlNode.get("joue").toString());
                    classement.put("pts", xmlNode.get("pts").toString());
                    classements.add(classement);
                }
        );
        json.put("etag", md5Digest(json.toString()));
        return json;
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
        jsonFiche.put("etag", md5Digest(jsonFiche.toString()));
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

        jsonFiche.put("etag", md5Digest(jsonFiche.toString()));
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
        json.put("etag", md5Digest(json.toString()));
        return json;
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
