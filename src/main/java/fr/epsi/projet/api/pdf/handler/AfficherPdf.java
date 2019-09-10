/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.epsi.projet.api.pdf.handler;

import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mjj19
 */
public class AfficherPdf extends HttpServlet {

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
        // récupération du nom du fichier à afficher dans les paramètres de la requête :
        String fileName = request.getParameter("name");
        // recuperation du type d'operation qu'on souhaite realiser avec le pdf (view, split, concatene) :
        String typeOperation = request.getParameter("typeOperation");
        if (typeOperation == null || "view".equals(typeOperation)) {
            viewPdf(fileName, response.getOutputStream());
        } else if ("concatene".equals(typeOperation)) {
            String fileName2 = request.getParameter("name2");
            OutputStream outputStream = response.getOutputStream();
            concatenePdf(fileName, fileName2, outputStream);
        } else if ("split".equals(typeOperation)) {
            int startPage = Integer.parseInt(request.getParameter("startPage"));
            int endPage = Integer.parseInt(request.getParameter("endPage"));
            OutputStream outputStream = response.getOutputStream();
            splitPdf(fileName, outputStream, startPage, endPage);
        }
    }

    private void concatenePdf(String fileName, String fileName2, OutputStream outputStream) throws IOException {
        // création de la variable pdf avec le chemin absolu du fichier concerné :
        File pdf = new File("C:\\test\\pdf\\" + fileName);
        File pdf2 = new File("C:\\test\\pdf\\" + fileName2);
        // lecture des fichiers :
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(pdf));
        PdfDocument pdfDocument2 = new PdfDocument(new PdfReader(pdf2));
        
        // création de l'objet pour écrire le pdf :
        PdfDocument pdfWriter = new PdfDocument(new PdfWriter(outputStream));
        // copie de pdfDocument et pdfDocument2 dans pdfWriter :
        pdfDocument.copyPagesTo(1, pdfDocument.getNumberOfPages(), pdfWriter, new PdfPageFormCopier());
        pdfDocument2.copyPagesTo(1, pdfDocument2.getNumberOfPages(), pdfWriter, new PdfPageFormCopier());
        // fermeture des flux :
        pdfDocument.close();
        pdfDocument2.close();
        pdfWriter.close();
    }

    private void splitPdf(String fileName, OutputStream outputStream, int startPage, int endPage) throws IOException {
        // création de la variable pdf avec le chemin absolu du fichier concerné :
        File pdf = new File("C:\\test\\pdf\\" + fileName);
        // lecture du fichier :
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(pdf));

        // création de l'objet pour écrire le pdf :
        PdfDocument pdfDocument2 = new PdfDocument(new PdfWriter(outputStream));
        // copie du pdfDocument dans pdfDocument2 :
        pdfDocument.copyPagesTo(startPage, endPage, pdfDocument2, new PdfPageFormCopier());
        // fermeture des flux :
        pdfDocument.close();
        pdfDocument2.close();
    }

    private void viewPdf(String fileName, OutputStream outputStream) throws IOException {
        // création de la variable pdf avec le chemin absolu du fichier concerné :
        File pdf = new File("C:\\test\\pdf\\" + fileName);
        // lecture du fichier :
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(pdf));

        // création de l'objet pour écrire le pdf :
        PdfDocument pdfDocument2 = new PdfDocument(new PdfWriter(outputStream));
        // copie du pdfDocument dans pdfDocument2 :
        pdfDocument.copyPagesTo(1, pdfDocument.getNumberOfPages(), pdfDocument2, new PdfPageFormCopier());
        // fermeture des flux :
        pdfDocument.close();
        pdfDocument2.close();
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
