/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package p2prueba2parcial2;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author ljmc2
 */
public class P2prueba2parcial2 extends JFrame {

    private JTextField pathFld;
    private JTextField busquedaFld;
    private JTextArea resultadosArea;
    private JButton archivosBtn;
    private JButton buscarBtn;

    private int contadorTexto = 0;
    private int contadorJava = 0;
    private int contadorPDF = 0;
    private int contadorOtros = 0;

    public P2prueba2parcial2() {
        setTitle("Analizador de Archivos");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new GridLayout(4, 2, 5, 5));
        panelDatos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelDatos.add(new JLabel("Ruta para directorio:"));
        pathFld = new JTextField();
        panelDatos.add(pathFld);
        panelDatos.add(new JLabel("Texto a buscar:"));
        busquedaFld = new JTextField();
        panelDatos.add(busquedaFld);

        archivosBtn = new JButton("Contar por extensión");
        panelDatos.add(archivosBtn);
        buscarBtn = new JButton("Buscar por nombre");
        panelDatos.add(buscarBtn);

        resultadosArea = new JTextArea();
        resultadosArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(resultadosArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));

        panelPrincipal.add(panelDatos, BorderLayout.NORTH);
        panelPrincipal.add(scroll, BorderLayout.CENTER);
        add(panelPrincipal);

        archivosBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                contarArchivos();
            }
        });
                
        buscarBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busqueda();
            }
        });
    }

    private boolean validarPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar una ruta.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        File dir = new File(path.trim());
        if (!dir.exists()) {
            JOptionPane.showMessageDialog(this, "Ingrese una ruta.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!dir.isDirectory()) {
            JOptionPane.showMessageDialog(this, "Esta ruta no es un directorio válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void contarArchivos() {
        String path = pathFld.getText();
        if (!validarPath(path)) {
            return;
        }

        contadorTexto = 0;
        contadorJava = 0;
        contadorPDF = 0;
        contadorOtros = 0;
        
        File dir=new File(path.trim());
        contarArchivosRec(dir);
        
        resultadosArea.setText("******CONTEO DE ARCHIVOS POR EXTENSIÓN******\n");
        resultadosArea.append("TXT:    "+contadorTexto+"archivo(s)\n");
        resultadosArea.append("JAVA:   "+contadorJava+"archivo(s)\n");
        resultadosArea.append("OTROS:  "+contadorPDF+"archivo(s)\n");
        resultadosArea.append("PDF:    "+contadorOtros+"archivo(s)\n\n");
        resultadosArea.append("Total:" + (contadorTexto + contadorJava + contadorPDF + contadorOtros) + " archivo(s)");
    }
    
    private void contarArchivosRec(File dir){
        File[] archivos=dir.listFiles();
        if(archivos==null)
            return;
        
        for(File file:archivos){
            if(file.isDirectory()){
                contarArchivosRec(file);
            }else{
                contadorTipo(file.getName());
            }
        }
    }
    
    private void contadorTipo(String name){
        String nombre=name.toLowerCase();
        if(nombre.endsWith(".txt")){
            contadorTexto++;
        }else if(nombre.endsWith(".java")){
            contadorJava++;
        }else if(nombre.endsWith(".pdf")){
            contadorPDF++;
        }else{
            contadorOtros++;
        }
    }
    
    private void busqueda(){
        String path=pathFld.getText();
        if(!validarPath(path))
            return;
        
        String busq=busquedaFld.getText().trim();
        if(busq.isEmpty()){
            JOptionPane.showMessageDialog(this, "Ingrese el texto a buscar.","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        StringBuilder resultados=new StringBuilder();
        resultados.append("*****BÚSQUEDA DE ARCHIVOS: \"").append(busq).append("\" *****\n\n");
        
        File dir=new File(path.trim());
        buscarArchivos(dir, busq.toLowerCase(),resultados);
        
        if(resultados.toString().equals("*****BÚSQUEDA DE ARCHIVOS: \""+busq+"\" *****\n\n")){
            resultados.append("No se encontraron archivos que coinciden con la búsqueda.");
        }
        
        resultadosArea.setText(resultados.toString());
    }
    
    private void buscarArchivos(File dir, String textoBusq, StringBuilder result){
        File[] archivos=dir.listFiles();
        if(archivos==null)
            return;
        
        for(File archivo:archivos){
            if(archivo.isDirectory()){
                buscarArchivos(archivo,textoBusq,result);//rec
            }else{
                if(archivo.getName().toLowerCase().contains(textoBusq)){
                    result.append(archivo.getAbsolutePath()).append("\n");
                }
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new P2prueba2parcial2().setVisible(true);
            }
        });
    }
    

}
