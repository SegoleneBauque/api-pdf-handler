/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.epsi.projet.api.pdf.handler;

import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;



/**
 *
 * @author mjj19
 */
@Path("pdf")
public class AfficherPdf {

    @Context
    private UriInfo context;

    public AfficherPdf() {
    }

    @GET
    @Path("concatene") // http://localhost:8080/api-pdf-handler/resources/pdf/concatene?name=resultat.pdf&name2=pdf-sample.pdf
    @Produces("application/pdf")
    public Response concatenePdf(@QueryParam("name") String fileName, @QueryParam("name2") String fileName2) {
        // creation de la variable pdf avec le chemin absolu des fichiers concernes :
        File pdf = new File("C:\\test\\pdf\\" + fileName);
        File pdf2 = new File("C:\\test\\pdf\\" + fileName2);
        // lecture des fichiers :
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfReader(pdf));
            PdfDocument pdfDocument2 = new PdfDocument(new PdfReader(pdf2));
            // creation d'un tableau de bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // creation de l'objet pour ecrire le pdf et le transformer en tableau de bytes :
            PdfDocument pdfWriter = new PdfDocument(new PdfWriter(baos));
            // copie de pdfDocument et pdfDocument2 dans pdfWriter :
            pdfDocument.copyPagesTo(1, pdfDocument.getNumberOfPages(), pdfWriter, new PdfPageFormCopier());
            pdfDocument2.copyPagesTo(1, pdfDocument2.getNumberOfPages(), pdfWriter, new PdfPageFormCopier());
            // fermeture des flux :
            pdfDocument.close();
            pdfDocument2.close();
            pdfWriter.close();
            baos.close();

            return Response
                    .status(Response.Status.OK)
                    .type("application/pdf")
                    .header("Access-Control-Allow-Origin", "*")
                    .entity(baos.toByteArray())
                    .build();
        } catch (IOException e) {
            return Response.status(Response.Status.NO_CONTENT)
                    .build();
        }
    }

    @GET
    @Path("split") // http://localhost:8080/api-pdf-handler/resources/pdf/split?name=pdf-sample.pdf&startPage=2&endPage=3
    @Produces("application/pdf")
    public Response splitPdf(@QueryParam("name") String fileName, @QueryParam("startPage") int startPage, @QueryParam("endPage") int endPage) {
        // création de la variable pdf avec le chemin absolu du fichier concerné :
        File pdf = new File("C:\\test\\pdf\\" + fileName);
        // lecture du fichier :
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfReader(pdf));
            // creation d'un tableau de bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // création de l'objet pour écrire le pdf :
            PdfDocument pdfDocument2 = new PdfDocument(new PdfWriter(baos));
            // copie du pdfDocument dans pdfDocument2 :
            pdfDocument.copyPagesTo(startPage, endPage, pdfDocument2, new PdfPageFormCopier());
            // fermeture des flux :
            pdfDocument.close();
            pdfDocument2.close();
            baos.close();
            return Response
                    .status(Response.Status.OK)
                    .type("application/pdf")
                    .header("Access-Control-Allow-Origin", "*")
                    .entity(baos.toByteArray())
                    .build();
        } catch (IOException e) {
            return Response.status(Response.Status.NO_CONTENT)
                    .build();
        }
    }

    @GET
    @Path("view") // http://localhost:8080/api-pdf-handler/resources/pdf/view?name=pdf-sample.pdf
    @Produces("application/pdf")
    public Response viewPdf(@QueryParam("name") String fileName) throws IOException {
        // création de la variable pdf avec le chemin absolu du fichier concerné :
        File pdf = new File("C:\\test\\pdf\\" + fileName);
        // lecture du fichier :
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(pdf));
        // creation d'un tableau de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // création de l'objet pour écrire le pdf :
        PdfDocument pdfDocument2 = new PdfDocument(new PdfWriter(baos));
        // copie du pdfDocument dans pdfDocument2 :
        pdfDocument.copyPagesTo(1, pdfDocument.getNumberOfPages(), pdfDocument2, new PdfPageFormCopier());
        // fermeture des flux :
        pdfDocument.close();
        pdfDocument2.close();
        baos.close();
        return Response
                .status(Response.Status.OK)
                .type("application/pdf")
                .header("Access-Control-Allow-Origin", "*")
                .entity(baos.toByteArray())
                .build();
    }

    @GET
    @Path("addImage") // http://localhost:8080/api-pdf-handler/resources/pdf/addImage?name=pdf-sample.pdf&name2=sego.png
    @Produces("application/pdf")
    public Response addImageToPdf(@QueryParam("name") String fileName, @QueryParam("name2") String imageName) throws IOException {
        // création de la variable pdf avec le chemin absolu du fichier concerné :
        File pdf = new File("C:\\test\\pdf\\" + fileName);
        
        // creation d'un tableau de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // modification du PDF et enregistrement en outputStream :
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(pdf), new PdfWriter(baos));
        // Document sur lequel on veut ajouter un élément (paragraphes, images etc.) :
        Document document = new Document(pdfDocument);

        // chargement de l'image sur le disque
        ImageData imageData = ImageDataFactory.create("C:\\test\\pdf\\" + imageName);
        // creation d'une image et fourniture des parametres. Page numero = 1 :
        Image image = new Image(imageData).scaleAbsolute(50 * imageData.getWidth() / imageData.getHeight(), 50).setFixedPosition(1, 25, 25);
        // Ajout de l'image sur la page
        document.add(image);

        // Clôture du flux :
        document.close();
        baos.close();
        return Response
                .status(Response.Status.OK)
                .type("application/pdf")
                .header("Access-Control-Allow-Origin", "*")
                .entity(baos.toByteArray())
                .build();
    }
    
   @POST
   @Path("/upload") // http://localhost:8080/api-pdf-handler/resources/pdf/upload?file=resultat.pdf
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail) {
       String fileLocation = "C:\\test\\pdf\\" + fileDetail.getFileName();
       //saving file
       try {
           Files.copy(uploadedInputStream, new File(fileLocation).toPath());
       } catch (IOException e) {
           e.printStackTrace();
       }
       String output = "File successfully uploaded to : " + fileLocation;
       return Response.status(200).entity(output).build();
   }
}
