package de.flexiprovider.core.kdf;

import de.flexiprovider.api.KeyDerivation;
import de.flexiprovider.api.Mac;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.InvalidKeyException;
import de.flexiprovider.api.exceptions.InvalidKeySpecException;
import de.flexiprovider.api.keys.SecretKey;
import de.flexiprovider.api.keys.SecretKeyFactory;
import de.flexiprovider.api.keys.SecretKeySpec;
import de.flexiprovider.api.parameters.AlgorithmParameterSpec;
import de.flexiprovider.common.util.BigEndianConversions;
import de.flexiprovider.common.util.ByteUtils;
import de.flexiprovider.core.mac.HMac;
import de.flexiprovider.core.mac.HMacKeyFactory;

/**
 * This class implements the PBKDF2 key derivation function as specified in <a
 * href="http://www.rsa.com/rsalabs/node.asp?id=2127">PKCS #5 v2.0</a>.
 * 
 * @author Martin D_ring
 */
public class PBKDF2 extends KeyDerivation {

    /**
     * The OID of PBKDF2.
     */
    public static final String OID = "1.2.840.113549.1.5.12";

    // the underlying MAC (HMACwithSHA1)
    private Mac mac = new HMac.SHA1();

    // the MAC key
    private SecretKey hmacKey;

    // the salt
    private byte[] salt;

    // the iteration count
    private int iterationCount;

    /**
     * Initialize this KDF with a secret and parameters. The supported
     * parameters type is {@link PBKDF2ParameterSpec}.
     * 
     * @param secret
     *                the secret from which to derive the key
     * @param params
     *                the parameters
     * @throws de.flexiprovider.api.exceptions.InvalidKeyException
     *                 if the secret is <tt>null</tt>.
     * @throws de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException
     *                 if the parameters are not an instance of
     *                 {@link PBKDF2ParameterSpec}.
     */
    public void init(byte[] secret, AlgorithmParameterSpec params)
	    throws InvalidKeyException, InvalidAlgorithmParameterException {

	// assure that secret is not null
	if (secret == null) {
	    throw new InvalidKeyException("null");
	}

	// generate an HMac key from the secret
	SecretKeySpec hmacKeySpec = new SecretKeySpec(secret, "HmacSHA1");
	SecretKeyFactory skf = new HMacKeyFactory();
	try {
	    hmacKey = skf.generateSecret(hmacKeySpec);
	} catch (InvalidKeySpecException e) {
	    // the key spec is correct and must be accepted
	    throw new RuntimeException("internal error");
	}

	if (!(params instanceof PBKDF2ParameterSpec)) {
	    throw new InvalidAlgorithmParameterException("unsupported type");
	}
	PBKDF2ParameterSpec kdfParams = (PBKDF2ParameterSpec) params;

	salt = kdfParams.getSalt();
	iterationCount = kdfParams.getIterationCount();
    }

    /**
     * Start the derivation process and return the derived key. If supported by
     * the concrete implementation, the derived key will be of the specified
     * length.
     * 
     * @param keySize
     *                the desired length of the derived key
     * @return the derived key with the specified length, or <tt>null</tt> if
     *         the key size is <tt>&lt; 0</tt>.
     */
    public byte[] deriveKey(int keySize) {
	if (keySize < 0) {
	    return null;
	}

	// hLen = output size of the MAC
	int hLen = mac.getMacLength();

	// l = keySize / hlen rounded up
	int l = (keySize + hLen - 1) / hLen;

	// array to temporarily store the key
	byte[] tempKey = new byte[l * hLen];
	for (int i = 0, pos = 0; i < l; i++) {
	    byte[] ti = f(i);
	    System.arraycopy(ti, 0, tempKey, pos, ti.length);
	    // ti.length should always be hLen
	    pos += ti.length;
	}

	byte[] result = new byte[keySize];
	System.arraycopy(tempKey, 0, result, 0, keySize);

	return result;
    }

    /**
     * A helper method. It "calculates" the exclusive-or sum of the first
     * <tt>iterationCount</tt> iterates of the underlying MAC applied to the
     * password <tt>key</tt> and the concatenation of the <tt>salt</tt> and
     * the <tt>block index</tt>.
     * 
     * @param i
     *                the block index
     * @return the generated bytes
     */
    private byte[] f(int i) {

	// convert i into 4 octets
	byte[] iBytes = BigEndianConversions.I2OSP(i);

	// concatenate salt and iBytes
	byte[] sAndI = ByteUtils.concatenate(salt, iBytes);

	try {
	    mac.init(hmacKey);
	} catch (InvalidKeyException e) {
	    // the key is correct and must be accepted
	    throw new RuntimeException("internal error");
	}
	byte[] u = mac.doFinal(sAndI);

	byte[] result = new byte[u.length];
	for (int j = 0; j < iterationCount; j++) {
	    ByteUtils.xor(result, u);
	    try {
		mac.init(hmacKey);
	    } catch (InvalidKeyException e) {
		// the key is correct and must be accepted
		throw new RuntimeException("internal error");
	    }
	    u = mac.doFinal(u);
	}
	ByteUtils.xor(result, u);

	return result;
    }

}
