/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.epsi.projet.api.pdf.handler;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static jdk.nashorn.internal.objects.NativeRegExp.source;

/**
 *
 * @author mjj19
 */
public class MergePdf extends HttpServlet {

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
        response.setContentType("application/pdf");
        // récupération du nom du fichier dans lequel on veut insérer une image dans les paramètres de la requête :
        String fileName = request.getParameter("name");
        // récupération de l'image à insérer dans les paramètres de la requête :
        String imageName = request.getParameter("image");
        addImageToPdf(fileName, imageName, response.getOutputStream());
    }

    private void addImageToPdf(String fileName, String imageName, OutputStream outputStream) throws IOException {
        // création de la variable pdf avec le chemin absolu du fichier concerné :
        File pdf = new File("C:\\test\\pdf\\" + fileName);
        // modification du PDF et enregistrement en outputStream :
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(pdf), new PdfWriter(outputStream));
        // Document sur lequel on veut ajouter un élément (paragraphes, images etc.) :
        Document document = new Document(pdfDocument);

        // chargement de l'image sur le disque
        ImageData imageData = ImageDataFactory.create("C:\\test\\pdf\\" +imageName);
        // creation d'une image et fourniture des parametres. Page numero = 1 :
        Image image = new Image(imageData).scaleAbsolute(100, 100).setFixedPosition(1, 25, 25);
        // Ajout de l'image sur la page
        document.add(image);

        // Clôture du flux :
        document.close();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
