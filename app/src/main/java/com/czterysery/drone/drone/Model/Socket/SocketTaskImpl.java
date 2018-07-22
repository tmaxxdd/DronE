package com.czterysery.drone.drone.Model.Socket;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.czterysery.drone.drone.Model.Socket.SocketTask;
import com.czterysery.drone.drone.Presenter.ControlPresenter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketTaskImpl implements SocketTask {
    private String TAG = this.getClass().getSimpleName();
    private ControlPresenter controlPresenter;
    private SocketAsyncTask socketAsyncTask;

    // Location of the remote host
    String address;
    int port;

    // Special messages denoting connection status
    private static final String PING_MSG = "SOCKET_PING";
    private static final String CONNECTED_MSG = "SOCKET_CONNECTED";
    private static final String DISCONNECTED_MSG = "SOCKET_DISCONNECTED";

    Socket socket = null;
    BufferedReader inStream = null;
    OutputStream outStream = null;

    // Signal to disconnectFromSocket from the socket
    private boolean disconnectSignal = false;

    // Socket timeout - close if no messages received (ms)
    private int timeout = 5000;

    // Constructor
    public SocketTaskImpl(ControlPresenter controlPresenter, String address, int port) {
        this.controlPresenter = controlPresenter;
        this.address = address;
        this.port = port;
    }

    @Override
    public void run(){

        //Allow to access network operations on Android 7.0.0 and higher
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (socketAsyncTask!= null){
            controlPresenter.setStatus("Already connected!");
            return;
        }

        try {
            socketAsyncTask = new SocketAsyncTask();
            socketAsyncTask.execute();
        }catch (Exception e){
            e.printStackTrace();
            controlPresenter.setStatus("Can't connect to the socket");
        }
    }

    /**
     * Write a message to the connection. Runs in UI thread.
     */
    @Override
    public void sendMessage(String message) {
        try {
            outStream.write(message.getBytes());
            outStream.write('\n');
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set a flag to disconnectFromSocket from the socket.
     */
    @Override
    public void disconnect() {
        disconnectSignal = true;
    }

    /**
     * AsyncTask that connects to a remote host over WiFi and reads/writes the connection
     * using a socket. The read loop of the AsyncTask happens in a separate thread, so the
     * main UI thread is not blocked. However, the AsyncTask has a way of sending data back
     * to the UI thread. Under the hood, it is using Threads and Handlers.
     */
    public class SocketAsyncTask extends AsyncTask<Void, String, Void> {

        /**
         * Main method of AsyncTask, opens a socket and continuously reads from it
         */
        @Override
        protected Void doInBackground(Void... arg) {

            try {
                // Open the socket and connect to it
                socket = new Socket();
                socket.connect(new InetSocketAddress(address, port), timeout);

                // Get the input and output streams
                inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outStream = socket.getOutputStream();

                // Confirm that the socket opened
                if (socket.isConnected()) {

                    // Make sure the input stream becomes ready, or timeout
                    long start = System.currentTimeMillis();
                    while (!inStream.ready()) {
                        long now = System.currentTimeMillis();
                        if (now - start > timeout) {
                            Log.e(TAG, "Input stream timeout, disconnecting!");
                            disconnectSignal = true;
                            break;
                        }
                    }
                } else {
                    Log.e(TAG, "Socket did not connect!");
                    disconnectSignal = true;
                }

                // Read messages in a loop until disconnected
                while (!disconnectSignal) {

                    // Parse a message with a newline character
                    String msg = inStream.readLine();

                    // Send it to the UI thread
                    publishProgress(msg);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Error in socket thread!");
            }

            // Send a disconnectFromSocket message
            publishProgress(DISCONNECTED_MSG);

            // Once disconnected, try to close the streams
            try {
                if (socket != null) socket.close();
                if (inStream != null) inStream.close();
                if (outStream != null) outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * This function runs in the UI thread but receives data from the
         * doInBackground() function running in a separate thread when
         * publishProgress() is called.
         */
        @Override
        protected void onProgressUpdate(String... values) {

            String msg = values[0];
            if (msg == null) return;

            // Handle meta-messages
            switch (msg) {
                case CONNECTED_MSG:
                    controlPresenter.onSocketSuccess(socket.getInetAddress().getHostName());
                    break;
                case DISCONNECTED_MSG:
                    controlPresenter.onSocketFailure(socket.getInetAddress().getHostName());
                    controlPresenter.setSocketPing(false);
                    break;
                case PING_MSG:
                    controlPresenter.setSocketPing(true);
                    break;
                // Invoke the gotMessage callback for all other messages
                default:
                    controlPresenter.gotMessage(msg);
                    break;
            }

            super.onProgressUpdate(values);
        }

    }
}