import java.net.*;
import java.io.*;

class clienteUDP {
    private static final int _PUERTO = 1234;
    
    public static void main(String args[]){
        
        InetAddress ipServidor = null;
        try{
            ipServidor = InetAddress.getByName(args[0]);
        } catch (UnknownHostException uhe){
            System.err.println("Host no encontrado : " + uhe);
            System.exit(-1);
        }
        
        DatagramSocket dgmSocket = null;
        try{
            dgmSocket = new DatagramSocket();
        } catch (SocketException se){
            System.err.println("Error al abrir el socket : " + se);
            System.exit(-1);
        }
        
        for (int n = 1; n < args.length; n++){
            try{
                
                ByteArrayOutputStream arrayEnvio = new ByteArrayOutputStream();
                
                DataOutputStream datosEnvio = new DataOutputStream(arrayEnvio);

                int numero = Integer.parseInt(args[n]);
                
                datosEnvio.writeInt(numero);
                
                datosEnvio.close();
                
                DatagramPacket dgmPaquete = new DatagramPacket(arrayEnvio.toByteArray(), 4, ipServidor, _PUERTO);
                
                dgmSocket.send(dgmPaquete);

                
                byte bufferEntrada[] = new byte[8];
                
                dgmPaquete = new DatagramPacket(bufferEntrada, 8);
                
                dgmSocket.receive(dgmPaquete);

                
                ByteArrayInputStream arrayRecepcion = new ByteArrayInputStream(bufferEntrada);
                DataInputStream datosEntrada = new DataInputStream(arrayRecepcion);
                
                long resultado = datosEntrada.readLong();
                
                System.out.println("Enviado = " + numero + "\tRecibido = " + resultado);
            } catch (Exception e){
                System.err.println("Se ha producido un error: " + e);
            }
        }
    }
}