package E2;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;

public class servidoUDP {
    // Configuramos la IP  y el Puerto para el servidor
    private static final String _IP = "192.168.1.69";
    private static final int _PUERTO = 1234;

    public static void main(String args[]) throws UnknownHostException {
        // Creamos instancia de clase InetAddress para indicar el host donde se inicia el servidor
        InetAddress ip = InetAddress.getByName(_IP);

        //Usamos un manejador  de formato para el log del servidor
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        //  Mostramos por consola los datos del servidor
        try{
            //Si se usa localhost:
            //System.out.println("IP de LocalHost = " + InetAddress.getLocalHost().toString());
            //si se usa una ip
            System.out.println("\nEscuchando en: ");
            System.out.println("IP Host = " + ip.getHostAddress());
            System.err.println("Puerto = " + _PUERTO + "\n");

        } catch (UnknownHostException ex){
            System.err.println("No puede saber la direccion IP local: " + ex);
        }

        // A traves de esta Socket enviaremos detagramas del tipo DatagramPacket
        DatagramSocket dgmSocket = null;
        try{
            dgmSocket = new DatagramSocket(_PUERTO, ip);
        } catch (SocketException se){
            System.err.println("Se ha producido un error al abrir el socket: " + se);
            System.exit(-1);

        }
        // Bucle  infinito de escucha
        while(true) {
            try {
                //Nos preparamos a recibir un numero entero
                byte bufferEntrada[] = new byte[4];

                //Creamos un contener de datagrama, cuyo buffer sera el array creado antes

                DatagramPacket dgmPaquete= new DatagramPacket(bufferEntrada, 4);

                //Esperamos a recibir un paquete
                dgmSocket.receive(dgmPaquete);

                //Podemos extraer informacion del paquete: 

                //N° de puerto desde donde se envio
                int puertoRemitente = dgmPaquete.getPort();
                //Direccion de Internet desde donde se envio
                InetAddress ipRemitente = dgmPaquete.getAddress();

                //Envolvemos el buffer con un ByteArrayInputStream...
                ByteArrayInputStream arrayEntrada = new ByteArrayInputStream(bufferEntrada);
                //... que volvemos a envolver con un DataInputStream
                DataInputStream datosEntrada = new DataInputStream(arrayEntrada);
                // Y leemos un numero entero a partir del array de bytes
                int entrada = datosEntrada.readInt();

                //hacemos los calculos que correspondan
                long salida =(long) entrada * (long) entrada;

                //Creamos un ByteArrayOutputStream sobre el que podemos escribir
                ByteArrayOutputStream arraySalida = new ByteArrayOutputStream();
                // LO envolvemos con un DataOutputStream
                DataOutputStream datosSalida = new DataOutputStream(arraySalida);
                // Escribimos el resultados, que debe ocupar 8 bytes
                datosSalida.writeLong(salida);

                //Cerramos el buffer de escritura 
                datosSalida.close();

                //Generamos el paquete de vuelta, usando los datos del remitente del paquete original
                dgmPaquete = new DatagramPacket(arraySalida.toByteArray(), 8, ipRemitente, puertoRemitente);
                //Enviamos
                dgmSocket.send(dgmPaquete);

                //Registremos en salida estandard
                System.out.println(formatter.format(new Date()) + "\tCliente = "+ ipRemitente + ":" +
                                   puertoRemitente + "\tEntrada = " + entrada + "\tSalida = " + salida);


            } catch (Exception e){
                System.err.println("Se ha producido el error " + e);
            }
        }

    }
}
