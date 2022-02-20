package E2;

import java.net.*;
import java.io.*;

class clienteUDP {
    private static final int _PUERTO = 1234;
    public static void main(String args[]){
        //Se lee el primer parametro, donde se debe ir la  direccion IP del servidor

        InetAddress ipServidor = null;
        try{
            ipServidor = InetAddress.getByName(args[0]);
        } catch (UnknownHostException uhe){
            System.err.println("Host no encontrado : " + uhe);
            System.exit(-1);
        }
        //Creamos el socket
        DatagramSocket dgmSocket = null;
        try{
            dgmSocket = new DatagramSocket();
        } catch (SocketException se){
            System.err.println("Error al abrir el socket : " + se);
            System.exit(-1);
        }
        //Para cada uno de los argumento...
        for (int n = 1; n < args.length; n++){
            try{
                //Creamos un buffer para escribir
                ByteArrayOutputStream arrayEnvio = new ByteArrayOutputStream();
                //Envolvemos el buffer en fijo de datos de salida
                DataOutputStream datosEnvio = new DataOutputStream(arrayEnvio);

                int numero = Integer.parseInt(args[n]);
                //Escribimos en el flujo
                datosEnvio.writeInt(numero);
                // y cerramos el buffer
                datosEnvio.close();

                // Creamos paquete
                DatagramPacket dgmPaquete = new DatagramPacket(arrayEnvio.toByteArray(), 4, ipServidor, _PUERTO);
                //y lo mandamos
                dgmSocket.send(dgmPaquete);

                //Preparamos buffer para recibir numero de 8 bytes
                byte bufferEntrada[] = new byte[8];
                // Creamos el contenedor del paquete
                dgmPaquete = new DatagramPacket(bufferEntrada, 8);
                // y lo recibimos
                dgmSocket.receive(dgmPaquete);

                // Creamos un stream de lectura a partir del buffer
                ByteArrayInputStream arrayRecepcion = new ByteArrayInputStream(bufferEntrada);
                DataInputStream datosEntrada = new DataInputStream(arrayRecepcion);
                // Leemos el resultado final
                long resultado = datosEntrada.readLong();
                // Indicamos en pantalla
                System.out.println("Enviado = " + numero + "\tRecibido = " + resultado);
            } catch (Exception e){
                System.err.println("Se ha producido un error: " + e);
            }
        }
    }
}