/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author DGurrera
 */
public class HTTPServer {

    static final int PORT = 8080;
    
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        try (ServerSocket connect = new ServerSocket(PORT)){
            
            System.out.println("Server started.\nListening for connection on port : " + PORT + "...\n");
            
            // Listen until user halts the server
            while(true) {
                try (Socket client = connect.accept()) {
                    InputStream is = client.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    OutputStream os = client.getOutputStream();
                    PrintStream out = new PrintStream(os);
                    
                    String line;
                    String path = br.readLine();
                    String[] pathParse = path.split("(/)|( )");
                    System.out.println(pathParse[2]);
                    System.out.println(path);
                    
                    while(!(line = br.readLine()).equals(""))
                        System.out.println(line);
                    System.out.println();
                    
                    File tempFile = new File(pathParse[2] + ".html");
                    
                    if (tempFile.exists())
                        accept(tempFile, out);
                    else
                        notFound(out);
                    
                    br.close();
                    out.close();
                }
            }
        } catch (IOException ex) {
            System.err.println("Server Connection error : " + ex.getMessage());
        }
    }
    
    public static void accept(File tempFile, PrintStream out) throws Exception
    {
	FileReader file = new FileReader(tempFile);
	BufferedReader br = new BufferedReader(file);
	String fTemp = null;
	String f = null;
	while((fTemp = br.readLine()) != null)
	    f += fTemp;

	br.close();
	int len = f.length();

	FileReader nfile = new FileReader(tempFile);
	BufferedReader nbr = new BufferedReader(nfile);
	String line = null;

	out.println("HTTP/1.1 200 OK");
	out.println("Content-type: text/html");
	out.println("Content-length: " + len);
	out.println();

	while((line = nbr.readLine()) != null)
	    out.println(line);

	nbr.close();
    }

    public static void notFound(PrintStream out) throws Exception
    {
	BufferedReader nbr = new BufferedReader(new FileReader("notFound.html"));

	String fTemp = null;
	String f = null;
	while((fTemp = nbr.readLine()) != null)
	    f += fTemp;

	nbr.close();
	int len = f.length();

	out.println("HTTP/1.1 404 Not Found");
	out.println("Content-type: text/html");
	out.println("Current-length: " + len);
	out.println();

	BufferedReader br = new BufferedReader(new FileReader("notFound.html"));
	String line;
	while((line = br.readLine()) != null)
	    out.println(line);

	br.close();
    }
}
