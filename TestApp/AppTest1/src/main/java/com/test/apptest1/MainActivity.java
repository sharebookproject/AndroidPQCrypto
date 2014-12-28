package com.test.apptest1;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.*;
import android.text.method.*;
import android.util.*;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;

import de.flexiprovider.common.ies.IESParameterSpec;
import de.flexiprovider.core.FlexiCoreProvider;
import de.flexiprovider.ec.FlexiECProvider;
import de.flexiprovider.ec.parameters.CurveParams;
import de.flexiprovider.ec.parameters.CurveRegistry.BrainpoolP160r1;
import de.flexiprovider.pqc.FlexiPQCProvider;
import de.flexiprovider.pqc.ecc.ECCKeyGenParameterSpec;
import de.flexiprovider.pqc.ecc.mceliece.McElieceKeyPairGenerator;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public void encOnClick(View v) throws Exception{
        TextView encTxt = (TextView) findViewById(R.id.encTxt);
        TextView resultTxt = (TextView) findViewById(R.id.resultTxt);

        String inp = encTxt.getText().toString();
        String res = encryptAndDecryptTextSampleUsage(inp);
        resultTxt.setMovementMethod(new ScrollingMovementMethod());

        resultTxt.setText(Html.fromHtml(res));

    }

    //You can generate keypair by parameters or key size
    public static KeyPair generateKeyPair(){
        int k = 2048;
        int n = 2804;
        int t = 66;
        int KeySize = 256;

        KeyPair kp = null;
        Security.addProvider(new FlexiCoreProvider());
        Security.addProvider(new FlexiPQCProvider());

        McElieceKeyPairGenerator mcElieceKeyPairGenerator = new McElieceKeyPairGenerator();
        mcElieceKeyPairGenerator.initialize(KeySize, new SecureRandom());
        kp = mcElieceKeyPairGenerator.generateKeyPair();

        return kp;
    }

    // encrypt byte array with public key
    public static byte[] encrypt(PublicKey publicKey, byte[] mBytes) throws Exception {
        Security.addProvider(new FlexiCoreProvider());
        Security.addProvider(new FlexiPQCProvider());

        SecureRandom sr = new SecureRandom();

        Cipher cipher = Cipher.getInstance("McEliece", "FlexiPQC");

        cipher.init(Cipher.ENCRYPT_MODE, publicKey, sr);

        byte[] cBytes = cipher.doFinal(mBytes);

        return cBytes;
    }

    //decrypt byte array with private key
    public static byte[] decrypt(PrivateKey privateKey, byte[] mBytes) throws Exception {
        int KeySize = 256;
        byte[] dBytes = new byte[0];


        SecureRandom sr = new SecureRandom();

        Cipher cipher = Cipher.getInstance("McEliece", "FlexiPQC");

        ECCKeyGenParameterSpec ecParams = new ECCKeyGenParameterSpec(KeySize);

        cipher.init(Cipher.DECRYPT_MODE, privateKey, sr);

        dBytes = cipher.doFinal(mBytes);
        return dBytes;

    }

    //Usage: Encrypt And Decrypt Text and show in TextView
    public static String encryptAndDecryptTextSampleUsage(String text) throws Exception {

        KeyPair keyPair = generateKeyPair();

        PublicKey pubKey = keyPair.getPublic();
        PrivateKey privKey = keyPair.getPrivate();


        // Encrypt

        byte[] bytes = text.getBytes();
        byte[] result = encrypt(pubKey, bytes);

        String decStr = new String(decrypt(privKey, result), "UTF-8");

        String s = "<div><font color=#000000>Private Key (Base 64 Format):</font></div>"+Base64.encodeToString(privKey.getEncoded(), Base64.DEFAULT) +
                "<div><font color=#000000>Public Key (Base 64 Format):</font></div>"+Base64.encodeToString(pubKey.getEncoded(), Base64.DEFAULT)+
                "<div><font color=#000000>Encrypted Text (Base 64 Format):</font></div>" + Base64.encodeToString(result, Base64.DEFAULT) +
                "<div><font color=#000000>Decrypted Text (String):</font></div>" + decStr;

        return s;
    }

}
