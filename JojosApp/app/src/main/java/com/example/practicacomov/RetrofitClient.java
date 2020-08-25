package com.example.practicacomov;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl, Context context) {
        if (retrofit == null) {

            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                InputStream cacertIS = context.getResources().openRawResource(R.raw.cacert);
                //InputStream androidIS = context.getResources().openRawResource(R.raw.androidcert);
                Certificate cacert = certificateFactory.generateCertificate(cacertIS);
                //Certificate android = certificateFactory.generateCertificate(androidIS);
                cacertIS.close();
                //androidIS.close();
                //Log.i("CERT", cacert.toString());
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", cacert);
                //keyStore.setCertificateEntry("android", android);

                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);

                X509TrustManager x509TrustManager = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{x509TrustManager}, null);

                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.level(HttpLoggingInterceptor.Level.BODY);
                CertificatePinner certificatePinner = new CertificatePinner
                                                        .Builder()
                                                        .add("192.168.0.104", "sha256/CuIqkZb2whFOG4BKjTcuTn4LjKo4CsaCJ8MTQdtntIk=")
                                                        .build();
                OkHttpClient okHttpClient = new OkHttpClient
                                            .Builder()
                                            .hostnameVerifier(new HostnameVerifier() {
                                                @Override
                                                public boolean verify(String hostname, SSLSession session) {
                                                    return hostname.equals("192.168.0.104");
                                                }
                                            })
                                            .certificatePinner(certificatePinner)
                                            .addInterceptor(interceptor)
                                            .sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager)
                                            .build();
                retrofit = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();

            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            }
        }
        return retrofit;
    }



}
