package com.android.apksigner;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


//import android.annotation.TargetApi;
//import android.os.Build;

import com.android.apksig.ApkSigner;
import com.android.apksig.ApkVerifier;
import com.android.apksig.ApkSigner.SignerConfig;
import com.android.apksig.ApkSigner.SignerConfig.Builder;
import com.android.apksig.ApkVerifier.IssueWithParams;
import com.android.apksig.ApkVerifier.Result;
import com.android.apksig.ApkVerifier.Result.V1SchemeSignerInfo;
import com.android.apksig.ApkVerifier.Result.V2SchemeSignerInfo;
import com.android.apksig.apk.MinSdkVersionException;
import com.android.apksigner.HexEncoding;
import com.android.apksigner.OptionsParser;
import com.android.apksigner.OptionsParser.OptionsException;
import com.android.apksigner.PasswordRetriever;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.ECKey;
import java.security.interfaces.RSAKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class ApkSignerTool2 {
    private static final String VERSION = "0.8";
    private static final String HELP_PAGE_GENERAL = "help.txt";
    private static final String HELP_PAGE_SIGN = "help_sign.txt";
    private static final String HELP_PAGE_VERIFY = "help_verify.txt";

    public ApkSignerTool2() {
    }

//    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void main(String[] params) throws Exception {
        if (params.length != 0 && !"--help".equals(params[0]) && !"-h".equals(params[0])) {
            if ("--version".equals(params[0])) {
                System.out.println("0.8");
            } else {
                String cmd = params[0];

                try {
                    if ("sign".equals(cmd)) {
                        sign((String[])Arrays.copyOfRange(params, 1, params.length));
                    } else if ("verify".equals(cmd)) {
                        verify((String[])Arrays.copyOfRange(params, 1, params.length));
                    } else if ("help".equals(cmd)) {
                        printUsage("help.txt");
                    } else if ("version".equals(cmd)) {
                        System.out.println("0.8");
                    } else {
                        throw new com.android.apksigner.ApkSignerTool2.ParameterException("Unsupported command: " + cmd + ". See --help for supported commands");
                    }
                } catch (OptionsException | com.android.apksigner.ApkSignerTool2.ParameterException var3) {
                    System.err.println(var3.getMessage());
                    // System.exit(1);
                }
            }
        } else {
            printUsage("help.txt");
        }
    }

//    @TargetApi(Build.VERSION_CODES.O)
    public static String sign(String[] params) throws Exception {
        StringBuffer buffer = new StringBuffer();
        if (params.length == 0) {
            printUsage("help_sign.txt");
        } else {
            File outputApk = null;
            File inputApk = null;
            boolean verbose = false;
            boolean v1SigningEnabled = true;
            boolean v2SigningEnabled = true;
            boolean debuggableApkPermitted = true;
            int minSdkVersion = 1;
            boolean minSdkVersionSpecified = false;
            int maxSdkVersion = 2147483647;
            List<com.android.apksigner.ApkSignerTool2.SignerParams> signers = new ArrayList(1);
            com.android.apksigner.ApkSignerTool2.SignerParams signerParams = new com.android.apksigner.ApkSignerTool2.SignerParams();
            List<com.android.apksigner.ApkSignerTool2.ProviderInstallSpec> providers = new ArrayList();
            com.android.apksigner.ApkSignerTool2.ProviderInstallSpec providerParams = new com.android.apksigner.ApkSignerTool2.ProviderInstallSpec();
            OptionsParser optionsParser = new OptionsParser(params);
            String optionOriginalForm = null;

            String optionName;
            while((optionName = optionsParser.nextOption()) != null) {
                optionOriginalForm = optionsParser.getOptionOriginalForm();
                if ("help".equals(optionName) || "h".equals(optionName)) {
                    printUsage("help_sign.txt");
                    return "";
                }

                if ("out".equals(optionName)) {
                    outputApk = new File(optionsParser.getRequiredValue("Output file name"));
                } else if ("in".equals(optionName)) {
                    inputApk = new File(optionsParser.getRequiredValue("Input file name"));
                } else if ("min-sdk-version".equals(optionName)) {
                    minSdkVersion = optionsParser.getRequiredIntValue("Mininimum API Level");
                    minSdkVersionSpecified = true;
                } else if ("max-sdk-version".equals(optionName)) {
                    maxSdkVersion = optionsParser.getRequiredIntValue("Maximum API Level");
                } else if ("v1-signing-enabled".equals(optionName)) {
                    v1SigningEnabled = optionsParser.getOptionalBooleanValue(true);
                } else if ("v2-signing-enabled".equals(optionName)) {
                    v2SigningEnabled = optionsParser.getOptionalBooleanValue(true);
                } else if ("debuggable-apk-permitted".equals(optionName)) {
                    debuggableApkPermitted = optionsParser.getOptionalBooleanValue(true);
                } else if ("next-signer".equals(optionName)) {
                    if (!signerParams.isEmpty()) {
                        signers.add(signerParams);
                        signerParams = new com.android.apksigner.ApkSignerTool2.SignerParams();
                    }
                } else if ("ks".equals(optionName)) {
                    signerParams.keystoreFile = optionsParser.getRequiredValue("KeyStore file");
                } else if ("ks-key-alias".equals(optionName)) {
                    signerParams.keystoreKeyAlias = optionsParser.getRequiredValue("KeyStore key alias");
                } else if ("ks-pass".equals(optionName)) {
                    signerParams.keystorePasswordSpec = optionsParser.getRequiredValue("KeyStore password");
                } else if ("key-pass".equals(optionName)) {
                    signerParams.keyPasswordSpec = optionsParser.getRequiredValue("Key password");
                } else if ("pass-encoding".equals(optionName)) {
                    String charsetName = optionsParser.getRequiredValue("Password character encoding");

                    try {
                        signerParams.passwordCharset = PasswordRetriever.getCharsetByName(charsetName);
                    } catch (IllegalArgumentException var33) {
                        throw new com.android.apksigner.ApkSignerTool2.ParameterException("Unsupported password character encoding requested using --pass-encoding: " + charsetName);
                    }
                } else if ("v1-signer-name".equals(optionName)) {
                    signerParams.v1SigFileBasename = optionsParser.getRequiredValue("JAR signature file basename");
                } else if ("ks-type".equals(optionName)) {
                    signerParams.keystoreType = optionsParser.getRequiredValue("KeyStore type");
                } else if ("ks-provider-name".equals(optionName)) {
                    signerParams.keystoreProviderName = optionsParser.getRequiredValue("JCA KeyStore Provider name");
                } else if ("ks-provider-class".equals(optionName)) {
                    signerParams.keystoreProviderClass = optionsParser.getRequiredValue("JCA KeyStore Provider class name");
                } else if ("ks-provider-arg".equals(optionName)) {
                    signerParams.keystoreProviderArg = optionsParser.getRequiredValue("JCA KeyStore Provider constructor argument");
                } else if ("key".equals(optionName)) {
                    signerParams.keyFile = optionsParser.getRequiredValue("Private key file");
                } else if ("cert".equals(optionName)) {
                    signerParams.certFile = optionsParser.getRequiredValue("Certificate file");
                } else if (!"v".equals(optionName) && !"verbose".equals(optionName)) {
                    if ("next-provider".equals(optionName)) {
                        if (!providerParams.isEmpty()) {
                            providers.add(providerParams);
                            providerParams = new com.android.apksigner.ApkSignerTool2.ProviderInstallSpec();
                        }
                    } else if ("provider-class".equals(optionName)) {
                        providerParams.className = optionsParser.getRequiredValue("JCA Provider class name");
                    } else if ("provider-arg".equals(optionName)) {
                        providerParams.constructorParam = optionsParser.getRequiredValue("JCA Provider constructor argument");
                    } else {
                        if (!"provider-pos".equals(optionName)) {
                            throw new com.android.apksigner.ApkSignerTool2.ParameterException("Unsupported option: " + optionOriginalForm + ". See --help for supported options.");
                        }

                        providerParams.position = optionsParser.getRequiredIntValue("JCA Provider position");
                    }
                } else {
                    verbose = optionsParser.getOptionalBooleanValue(true);
                }
            }

            if (!signerParams.isEmpty()) {
                signers.add(signerParams);
            }

            signerParams = null;
            if (!providerParams.isEmpty()) {
                providers.add(providerParams);
            }

            providerParams = null;
            if (signers.isEmpty()) {
                throw new com.android.apksigner.ApkSignerTool2.ParameterException("At least one signer must be specified");
            } else {
                params = optionsParser.getRemainingParams();
                if (inputApk != null) {
                    if (params.length > 0) {
                        throw new com.android.apksigner.ApkSignerTool2.ParameterException("Unexpected parameter(s) after " + optionOriginalForm + ": " + params[0]);
                    }
                } else {
                    if (params.length < 1) {
                        throw new com.android.apksigner.ApkSignerTool2.ParameterException("Missing input APK");
                    }

                    if (params.length > 1) {
                        throw new com.android.apksigner.ApkSignerTool2.ParameterException("Unexpected parameter(s) after input APK (" + params[1] + ")");
                    }

                    inputApk = new File(params[0]);
                }

                if (minSdkVersionSpecified && minSdkVersion > maxSdkVersion) {
                    throw new com.android.apksigner.ApkSignerTool2.ParameterException("Min API Level (" + minSdkVersion + ") > max API Level (" + maxSdkVersion + ")");
                } else {
                    Iterator var39 = providers.iterator();

                    while(var39.hasNext()) {
                        com.android.apksigner.ApkSignerTool2.ProviderInstallSpec providerInstallSpec = (com.android.apksigner.ApkSignerTool2.ProviderInstallSpec)var39.next();
                        providerInstallSpec.installProvider();
                    }

                    List<SignerConfig> signerConfigs = new ArrayList(signers.size());
                    int signerNumber = 0;
                    PasswordRetriever passwordRetriever = new PasswordRetriever();
                    Throwable var20 = null;

                    String msg;
                    try {
                        Iterator var21 = signers.iterator();

                        while(var21.hasNext()) {
                            com.android.apksigner.ApkSignerTool2.SignerParams signer = (com.android.apksigner.ApkSignerTool2.SignerParams)var21.next();
                            ++signerNumber;
                            signer.name = "signer #" + signerNumber;

                            try {
                                signer.loadPrivateKeyAndCerts(passwordRetriever);
                            } catch (com.android.apksigner.ApkSignerTool2.ParameterException var35) {
                                System.err.println("Failed to load signer \"" + signer.name + "\": " + var35.getMessage());
                                buffer.append("Failed to load signer \"" + signer.name + "\": " + var35.getMessage()+"\n");
                                // System.exit(2);
                                return buffer.toString();
                            } catch (Exception var36) {
                                System.err.println("Failed to load signer \"" + signer.name + "\"");
                                buffer.append("Failed to load signer \"" + signer.name + "\""+"\n");
                                var36.printStackTrace();
                                // System.exit(2);
                                return buffer.toString();
                            }

                            if (signer.v1SigFileBasename != null) {
                                msg = signer.v1SigFileBasename;
                            } else if (signer.keystoreKeyAlias != null) {
                                msg = signer.keystoreKeyAlias;
                            } else {
                                if (signer.keyFile == null) {
                                    throw new RuntimeException("Neither KeyStore key alias nor private key file available");
                                }

                                String keyFileName = (new File(signer.keyFile)).getName();
                                int delimiterIndex = keyFileName.indexOf(46);
                                if (delimiterIndex == -1) {
                                    msg = keyFileName;
                                } else {
                                    msg = keyFileName.substring(0, delimiterIndex);
                                }
                            }

                            SignerConfig signerConfig = (new Builder(msg, signer.privateKey, signer.certs)).build();
                            signerConfigs.add(signerConfig);
                        }
                    } catch (Throwable var37) {
                        var20 = var37;
                        throw var37;
                    } finally {
                         closeResource(var20, passwordRetriever);
                    }

                    if (outputApk == null) {
                        outputApk = inputApk;
                    }

                    File tmpOutputApk;
                    if (inputApk.getCanonicalPath().equals(outputApk.getCanonicalPath())) {
                        tmpOutputApk = File.createTempFile("apksigner", ".apk");
                        tmpOutputApk.deleteOnExit();
                    } else {
                        tmpOutputApk = outputApk;
                    }

                    com.android.apksig.ApkSigner.Builder apkSignerBuilder = (new com.android.apksig.ApkSigner.Builder(signerConfigs)).setInputApk(inputApk).setOutputApk(tmpOutputApk).setOtherSignersSignaturesPreserved(false).setV1SigningEnabled(v1SigningEnabled).setV2SigningEnabled(v2SigningEnabled).setDebuggableApkPermitted(debuggableApkPermitted);
                    if (minSdkVersionSpecified) {
                        apkSignerBuilder.setMinSdkVersion(minSdkVersion);
                    }

                    ApkSigner apkSigner = apkSignerBuilder.build();

                    try {
                        apkSigner.sign();
                    } catch (MinSdkVersionException var34) {
                        msg = var34.getMessage();
                        if (!msg.endsWith(".")) {
                            msg = msg + '.';
                        }

                        throw new MinSdkVersionException("Failed to determine APK's minimum supported platform version. Use --min-sdk-version to override", var34);
                    }

                    if (!tmpOutputApk.getCanonicalPath().equals(outputApk.getCanonicalPath())) {
                        Files.move(tmpOutputApk.toPath(), outputApk.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }

                    if (verbose) {
                        System.out.println("Signed");
                        buffer.append("Signed");
                    }

                }
            }
        }
        return buffer.toString();
    }

//    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void verify(String[] params) throws Exception {
        if (params.length == 0) {
            printUsage("help_verify.txt");
        } else {
            File inputApk = null;
            int minSdkVersion = 1;
            boolean minSdkVersionSpecified = false;
            int maxSdkVersion = 2147483647;
            boolean maxSdkVersionSpecified = false;
            boolean printCerts = false;
            boolean verbose = false;
            boolean warningsTreatedAsErrors = false;
            OptionsParser optionsParser = new OptionsParser(params);
            String optionOriginalForm = null;

            while(true) {
                while(true) {
                    while(true) {
                        while(true) {
                            while(true) {
                                String optionName;
                                while((optionName = optionsParser.nextOption()) != null) {
                                    optionOriginalForm = optionsParser.getOptionOriginalForm();
                                    if (!"min-sdk-version".equals(optionName)) {
                                        if (!"max-sdk-version".equals(optionName)) {
                                            if (!"print-certs".equals(optionName)) {
                                                if (!"v".equals(optionName) && !"verbose".equals(optionName)) {
                                                    if (!"Werr".equals(optionName)) {
                                                        if ("help".equals(optionName) || "h".equals(optionName)) {
                                                            printUsage("help_verify.txt");
                                                            return;
                                                        }

                                                        if (!"in".equals(optionName)) {
                                                            throw new com.android.apksigner.ApkSignerTool2.ParameterException("Unsupported option: " + optionOriginalForm + ". See --help for supported options.");
                                                        }

                                                        inputApk = new File(optionsParser.getRequiredValue("Input APK file"));
                                                    } else {
                                                        warningsTreatedAsErrors = optionsParser.getOptionalBooleanValue(true);
                                                    }
                                                } else {
                                                    verbose = optionsParser.getOptionalBooleanValue(true);
                                                }
                                            } else {
                                                printCerts = optionsParser.getOptionalBooleanValue(true);
                                            }
                                        } else {
                                            maxSdkVersion = optionsParser.getRequiredIntValue("Maximum API Level");
                                            maxSdkVersionSpecified = true;
                                        }
                                    } else {
                                        minSdkVersion = optionsParser.getRequiredIntValue("Mininimum API Level");
                                        minSdkVersionSpecified = true;
                                    }
                                }

                                params = optionsParser.getRemainingParams();
                                if (inputApk != null) {
                                    if (params.length > 0) {
                                        throw new com.android.apksigner.ApkSignerTool2.ParameterException("Unexpected parameter(s) after " + optionOriginalForm + ": " + params[0]);
                                    }
                                } else {
                                    if (params.length < 1) {
                                        throw new com.android.apksigner.ApkSignerTool2.ParameterException("Missing APK");
                                    }

                                    if (params.length > 1) {
                                        throw new com.android.apksigner.ApkSignerTool2.ParameterException("Unexpected parameter(s) after APK (" + params[1] + ")");
                                    }

                                    inputApk = new File(params[0]);
                                }

                                if (minSdkVersionSpecified && maxSdkVersionSpecified && minSdkVersion > maxSdkVersion) {
                                    throw new com.android.apksigner.ApkSignerTool2.ParameterException("Min API Level (" + minSdkVersion + ") > max API Level (" + maxSdkVersion + ")");
                                }

                                com.android.apksig.ApkVerifier.Builder apkVerifierBuilder = new com.android.apksig.ApkVerifier.Builder(inputApk);
                                if (minSdkVersionSpecified) {
                                    apkVerifierBuilder.setMinCheckedPlatformVersion(minSdkVersion);
                                }

                                if (maxSdkVersionSpecified) {
                                    apkVerifierBuilder.setMaxCheckedPlatformVersion(maxSdkVersion);
                                }

                                ApkVerifier apkVerifier = apkVerifierBuilder.build();

                                Result result;
                                try {
                                    result = apkVerifier.verify();
                                } catch (MinSdkVersionException var28) {
                                    String msg = var28.getMessage();
                                    if (!msg.endsWith(".")) {
                                        msg = msg + '.';
                                    }

                                    throw new MinSdkVersionException("Failed to determine APK's minimum supported platform version. Use --min-sdk-version to override", var28);
                                }

                                boolean verified = result.isVerified();
                                boolean warningsEncountered = false;
                                if (verified) {
                                    List<X509Certificate> signerCerts = result.getSignerCertificates();
                                    if (verbose) {
                                        System.out.println("Verifies");
                                        System.out.println("Verified using v1 scheme (JAR signing): " + result.isVerifiedUsingV1Scheme());
                                        System.out.println("Verified using v2 scheme (APK Signature Scheme v2): " + result.isVerifiedUsingV2Scheme());
                                        System.out.println("Number of signers: " + signerCerts.size());
                                    }

                                    if (printCerts) {
                                        int signerNumber = 0;
                                        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
                                        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
                                        MessageDigest md5 = MessageDigest.getInstance("MD5");
                                        Iterator var22 = signerCerts.iterator();

                                        while(var22.hasNext()) {
                                            X509Certificate signerCert = (X509Certificate)var22.next();
                                            ++signerNumber;
                                            System.out.println("Signer #" + signerNumber + " certificate DN: " + signerCert.getSubjectDN());
                                            byte[] encodedCert = signerCert.getEncoded();
                                            System.out.println("Signer #" + signerNumber + " certificate SHA-256 digest: " + HexEncoding.encode(sha256.digest(encodedCert)));
                                            System.out.println("Signer #" + signerNumber + " certificate SHA-1 digest: " + HexEncoding.encode(sha1.digest(encodedCert)));
                                            System.out.println("Signer #" + signerNumber + " certificate MD5 digest: " + HexEncoding.encode(md5.digest(encodedCert)));
                                            if (verbose) {
                                                PublicKey publicKey = signerCert.getPublicKey();
                                                System.out.println("Signer #" + signerNumber + " key algorithm: " + publicKey.getAlgorithm());
                                                int keySize = -1;
                                                if (publicKey instanceof RSAKey) {
                                                    keySize = ((RSAKey)publicKey).getModulus().bitLength();
                                                } else if (publicKey instanceof ECKey) {
                                                    keySize = ((ECKey)publicKey).getParams().getOrder().bitLength();
                                                } else if (publicKey instanceof DSAKey) {
                                                    DSAParams dsaParams = ((DSAKey)publicKey).getParams();
                                                    if (dsaParams != null) {
                                                        keySize = dsaParams.getP().bitLength();
                                                    }
                                                }

                                                System.out.println("Signer #" + signerNumber + " key size (bits): " + (keySize != -1 ? String.valueOf(keySize) : "n/a"));
                                                byte[] encodedKey = publicKey.getEncoded();
                                                System.out.println("Signer #" + signerNumber + " public key SHA-256 digest: " + HexEncoding.encode(sha256.digest(encodedKey)));
                                                System.out.println("Signer #" + signerNumber + " public key SHA-1 digest: " + HexEncoding.encode(sha1.digest(encodedKey)));
                                                System.out.println("Signer #" + signerNumber + " public key MD5 digest: " + HexEncoding.encode(md5.digest(encodedKey)));
                                            }
                                        }
                                    }
                                } else {
                                    System.err.println("DOES NOT VERIFY");
                                }

                                Iterator var30 = result.getErrors().iterator();

                                while(var30.hasNext()) {
                                    IssueWithParams error = (IssueWithParams)var30.next();
                                    System.err.println("ERROR: " + error);
                                }

                                PrintStream warningsOut = warningsTreatedAsErrors ? System.err : System.out;
                                Iterator var33 = result.getWarnings().iterator();

                                while(var33.hasNext()) {
                                    IssueWithParams warning = (IssueWithParams)var33.next();
                                    warningsEncountered = true;
                                    warningsOut.println("WARNING: " + warning);
                                }

                                var33 = result.getV1SchemeSigners().iterator();

                                String signerName;
                                Iterator var38;
                                IssueWithParams warning;
                                while(var33.hasNext()) {
                                    V1SchemeSignerInfo signer = (V1SchemeSignerInfo)var33.next();
                                    signerName = signer.getName();
                                    var38 = signer.getErrors().iterator();

                                    while(var38.hasNext()) {
                                        warning = (IssueWithParams)var38.next();
                                        System.err.println("ERROR: JAR signer " + signerName + ": " + warning);
                                    }

                                    var38 = signer.getWarnings().iterator();

                                    while(var38.hasNext()) {
                                        warning = (IssueWithParams)var38.next();
                                        warningsEncountered = true;
                                        warningsOut.println("WARNING: JAR signer " + signerName + ": " + warning);
                                    }
                                }

                                var33 = result.getV2SchemeSigners().iterator();

                                while(var33.hasNext()) {
                                    V2SchemeSignerInfo signer = (V2SchemeSignerInfo)var33.next();
                                    signerName = "signer #" + (signer.getIndex() + 1);
                                    var38 = signer.getErrors().iterator();

                                    while(var38.hasNext()) {
                                        warning = (IssueWithParams)var38.next();
                                        System.err.println("ERROR: APK Signature Scheme v2 " + signerName + ": " + warning);
                                    }

                                    var38 = signer.getWarnings().iterator();

                                    while(var38.hasNext()) {
                                        warning = (IssueWithParams)var38.next();
                                        warningsEncountered = true;
                                        warningsOut.println("WARNING: APK Signature Scheme v2 " + signerName + ": " + warning);
                                    }
                                }

                                if (!verified) {
                                    // System.exit(1);
                                    return;
                                }

                                if (warningsTreatedAsErrors && warningsEncountered) {
                                    // System.exit(1);
                                    return;
                                }

                                return;
                            }
                        }
                    }
                }
            }
        }
    }

//    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void printUsage(String page) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(com.android.apksigner.ApkSignerTool2.class.getResourceAsStream(page), StandardCharsets.UTF_8));
            Throwable var2 = null;

            try {
                String line;
                try {
                    while((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (Throwable var8) {
                    var2 = var8;
                    throw var8;
                }
            } finally {
                closeResource(var2, in);
            }

        } catch (IOException var10) {
            throw new RuntimeException("Failed to read " + page + " resource");
        }
    }

//    @TargetApi(Build.VERSION_CODES.KITKAT)
    static void closeResource(Throwable ver2, AutoCloseable in){
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] readFully(File file) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        FileInputStream in = new FileInputStream(file);
        Throwable var3 = null;

        try {
            drain(in, result);
        } catch (Throwable var8) {
            var3 = var8;
            throw var8;
        } finally {
            closeResource(var3, in);
        }

        return result.toByteArray();
    }

    private static void drain(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[65536];

        int chunkSize;
        while((chunkSize = in.read(buf)) != -1) {
            out.write(buf, 0, chunkSize);
        }

    }

    private static class ParameterException extends Exception {
        private static final long serialVersionUID = 1L;

        ParameterException(String message) {
            super(message);
        }
    }

    private static class SignerParams {
        String name;
        String keystoreFile;
        String keystoreKeyAlias;
        String keystorePasswordSpec;
        String keyPasswordSpec;
        Charset passwordCharset;
        String keystoreType;
        String keystoreProviderName;
        String keystoreProviderClass;
        String keystoreProviderArg;
        String keyFile;
        String certFile;
        String v1SigFileBasename;
        PrivateKey privateKey;
        List<X509Certificate> certs;

        private SignerParams() {
        }

        private boolean isEmpty() {
            return this.name == null && this.keystoreFile == null && this.keystoreKeyAlias == null && this.keystorePasswordSpec == null && this.keyPasswordSpec == null && this.passwordCharset == null && this.keystoreType == null && this.keystoreProviderName == null && this.keystoreProviderClass == null && this.keystoreProviderArg == null && this.keyFile == null && this.certFile == null && this.v1SigFileBasename == null && this.privateKey == null && this.certs == null;
        }

        private void loadPrivateKeyAndCerts(PasswordRetriever passwordRetriever) throws Exception {
            if (this.keystoreFile != null) {
                if (this.keyFile != null) {
                    throw new com.android.apksigner.ApkSignerTool2.ParameterException("--ks and --key may not be specified at the same time");
                }

                if (this.certFile != null) {
                    throw new com.android.apksigner.ApkSignerTool2.ParameterException("--ks and --cert may not be specified at the same time");
                }

                this.loadPrivateKeyAndCertsFromKeyStore(passwordRetriever);
            } else {
                if (this.keyFile == null) {
                    throw new com.android.apksigner.ApkSignerTool2.ParameterException("KeyStore (--ks) or private key file (--key) must be specified");
                }

                this.loadPrivateKeyAndCertsFromFiles(passwordRetriever);
            }

        }

        private void loadPrivateKeyAndCertsFromKeyStore(PasswordRetriever passwordRetriever) throws Exception {
            if (this.keystoreFile == null) {
                throw new com.android.apksigner.ApkSignerTool2.ParameterException("KeyStore (--ks) must be specified");
            } else {
                String ksType = this.keystoreType != null ? this.keystoreType : KeyStore.getDefaultType();
                KeyStore ks;
                if (this.keystoreProviderName != null) {
                    ks = KeyStore.getInstance(ksType, this.keystoreProviderName);
                } else if (this.keystoreProviderClass != null) {
                    Class<?> ksProviderClass = Class.forName(this.keystoreProviderClass);
                    if (!Provider.class.isAssignableFrom(ksProviderClass)) {
                        throw new com.android.apksigner.ApkSignerTool2.ParameterException("Keystore Provider class " + this.keystoreProviderClass + " not subclass of " + Provider.class.getName());
                    }

                    Provider ksProvider;
                    if (this.keystoreProviderArg != null) {
                        ksProvider = (Provider)ksProviderClass.getConstructor(String.class).newInstance(this.keystoreProviderArg);
                    } else {
                        ksProvider = (Provider)ksProviderClass.getConstructor().newInstance();
                    }

                    ks = KeyStore.getInstance(ksType, ksProvider);
                } else {
                    ks = KeyStore.getInstance(ksType);
                }

                String keyAlias = this.keystorePasswordSpec != null ? this.keystorePasswordSpec : "stdin";
                Charset[] additionalPasswordEncodings = this.passwordCharset != null ? new Charset[]{this.passwordCharset} : new Charset[0];
                List<char[]> keystorePasswords = passwordRetriever.getPasswords(keyAlias, "Keystore password for " + this.name, additionalPasswordEncodings);
                loadKeyStoreFromFile(ks, "NONE".equals(this.keystoreFile) ? null : this.keystoreFile, keystorePasswords);
                keyAlias = null;
                PrivateKey key = null;

                try {
                    if (this.keystoreKeyAlias == null) {
                        Enumeration<String> aliases = ks.aliases();
                        if (aliases != null) {
                            while(aliases.hasMoreElements()) {
                                String entryAlias = (String)aliases.nextElement();
                                if (ks.isKeyEntry(entryAlias)) {
                                    if (this.keystoreKeyAlias != null) {
                                        throw new com.android.apksigner.ApkSignerTool2.ParameterException(this.keystoreFile + " contains multiple key entries. --ks-key-alias option must be used to specify which entry to use.");
                                    }

                                    this.keystoreKeyAlias = entryAlias;
                                }
                            }
                        }

                        if (this.keystoreKeyAlias == null) {
                            throw new com.android.apksigner.ApkSignerTool2.ParameterException(this.keystoreFile + " does not contain key entries");
                        }
                    }

                    keyAlias = this.keystoreKeyAlias;
                    if (!ks.isKeyEntry(keyAlias)) {
                        throw new com.android.apksigner.ApkSignerTool2.ParameterException(this.keystoreFile + " entry \"" + keyAlias + "\" does not contain a key");
                    }

                    Key entryKey;
                    if (this.keyPasswordSpec != null) {
                        List<char[]> keyPasswords = passwordRetriever.getPasswords(this.keyPasswordSpec, "Key \"" + keyAlias + "\" password for " + this.name, additionalPasswordEncodings);
                        entryKey = getKeyStoreKey(ks, keyAlias, keyPasswords);
                    } else {
                        try {
                            entryKey = getKeyStoreKey(ks, keyAlias, keystorePasswords);
                        } catch (UnrecoverableKeyException var13) {
                            List<char[]> keyPasswords = passwordRetriever.getPasswords("stdin", "Key \"" + keyAlias + "\" password for " + this.name, additionalPasswordEncodings);
                            entryKey = getKeyStoreKey(ks, keyAlias, keyPasswords);
                        }
                    }

                    if (entryKey == null) {
                        throw new com.android.apksigner.ApkSignerTool2.ParameterException(this.keystoreFile + " entry \"" + keyAlias + "\" does not contain a key");
                    }

                    if (!(entryKey instanceof PrivateKey)) {
                        throw new com.android.apksigner.ApkSignerTool2.ParameterException(this.keystoreFile + " entry \"" + keyAlias + "\" does not contain a private key. It contains a key of algorithm: " + entryKey.getAlgorithm());
                    }

                    key = (PrivateKey)entryKey;
                } catch (UnrecoverableKeyException var14) {
                    throw new IOException("Failed to obtain key with alias \"" + keyAlias + "\" from " + this.keystoreFile + ". Wrong password?", var14);
                }

                this.privateKey = key;
                Certificate[] certChain = ks.getCertificateChain(keyAlias);
                if (certChain != null && certChain.length != 0) {
                    this.certs = new ArrayList(certChain.length);
                    Certificate[] var20 = certChain;
                    int var21 = certChain.length;

                    for(int var11 = 0; var11 < var21; ++var11) {
                        Certificate cert = var20[var11];
                        this.certs.add((X509Certificate)cert);
                    }

                } else {
                    throw new com.android.apksigner.ApkSignerTool2.ParameterException(this.keystoreFile + " entry \"" + keyAlias + "\" does not contain certificates");
                }
            }
        }

        private static void loadKeyStoreFromFile(KeyStore ks, String file, List<char[]> passwords) throws Exception {
            Exception lastFailure = null;
            Iterator var4 = passwords.iterator();

            while(var4.hasNext()) {
                char[] password = (char[])var4.next();

                try {
                    if (file != null) {
                        FileInputStream in = new FileInputStream(file);
                        Throwable var7 = null;

                        try {
                            ks.load(in, password);
                        } catch (Throwable var13) {
                            var7 = var13;
                            throw var13;
                        } finally {
                            closeResource(var7, in);
                        }
                    } else {
                        ks.load((InputStream)null, password);
                    }

                    return;
                } catch (Exception var15) {
                    lastFailure = var15;
                }
            }

            if (lastFailure == null) {
                throw new RuntimeException("No keystore passwords");
            } else {
                throw lastFailure;
            }
        }

        private static Key getKeyStoreKey(KeyStore ks, String keyAlias, List<char[]> passwords) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
            UnrecoverableKeyException lastFailure = null;
            Iterator var4 = passwords.iterator();

            while(var4.hasNext()) {
                char[] password = (char[])var4.next();

                try {
                    return ks.getKey(keyAlias, password);
                } catch (UnrecoverableKeyException var7) {
                    lastFailure = var7;
                }
            }

            if (lastFailure == null) {
                throw new RuntimeException("No key passwords");
            } else {
                throw lastFailure;
            }
        }

        private void loadPrivateKeyAndCertsFromFiles(PasswordRetriever passwordRetriver) throws Exception {
            if (this.keyFile == null) {
                throw new com.android.apksigner.ApkSignerTool2.ParameterException("Private key file (--key) must be specified");
            } else if (this.certFile == null) {
                throw new com.android.apksigner.ApkSignerTool2.ParameterException("Certificate file (--cert) must be specified");
            } else {
                byte[] privateKeyBlob = com.android.apksigner.ApkSignerTool2.readFully(new File(this.keyFile));

                PKCS8EncodedKeySpec keySpec;
                try {
                    EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(privateKeyBlob);
                    String passwordSpec = this.keyPasswordSpec != null ? this.keyPasswordSpec : "stdin";
                    Charset[] additionalPasswordEncodings = this.passwordCharset != null ? new Charset[]{this.passwordCharset} : new Charset[0];
                    List<char[]> keyPasswords = passwordRetriver.getPasswords(passwordSpec, "Private key password for " + this.name, additionalPasswordEncodings);
                    keySpec = decryptPkcs8EncodedKey(encryptedPrivateKeyInfo, keyPasswords);
                } catch (IOException var16) {
                    if (this.keyPasswordSpec != null) {
                        throw new InvalidKeySpecException("Failed to parse encrypted private key blob " + this.keyFile, var16);
                    }

                    keySpec = new PKCS8EncodedKeySpec(privateKeyBlob);
                }

                try {
                    this.privateKey = loadPkcs8EncodedPrivateKey(keySpec);
                } catch (InvalidKeySpecException var15) {
                    throw new InvalidKeySpecException("Failed to load PKCS #8 encoded private key from " + this.keyFile, var15);
                }

                FileInputStream in = new FileInputStream(this.certFile);
                Throwable var20 = null;

                Collection certs;
                try {
                    certs = CertificateFactory.getInstance("X.509").generateCertificates(in);
                } catch (Throwable var13) {
                    var20 = var13;
                    throw var13;
                } finally {
                    closeResource(var20, in);
                }

                ArrayList certList = new ArrayList(certs.size());
                Iterator var21 = certs.iterator();

                while(var21.hasNext()) {
                    Certificate cert = (Certificate)var21.next();
                    certList.add((X509Certificate)cert);
                }

                this.certs = certList;
            }
        }

        private static PKCS8EncodedKeySpec decryptPkcs8EncodedKey(EncryptedPrivateKeyInfo encryptedPrivateKeyInfo, List<char[]> passwords) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encryptedPrivateKeyInfo.getAlgName());
            InvalidKeySpecException lastKeySpecException = null;
            InvalidKeyException lastKeyException = null;
            Iterator var5 = passwords.iterator();

            while(var5.hasNext()) {
                char[] password = (char[])var5.next();
                PBEKeySpec decryptionKeySpec = new PBEKeySpec(password);

                try {
                    SecretKey decryptionKey = keyFactory.generateSecret(decryptionKeySpec);
                    return encryptedPrivateKeyInfo.getKeySpec(decryptionKey);
                } catch (InvalidKeySpecException var9) {
                    lastKeySpecException = var9;
                } catch (InvalidKeyException var10) {
                    lastKeyException = var10;
                }
            }

            if (lastKeyException == null && lastKeySpecException == null) {
                throw new RuntimeException("No passwords");
            } else if (lastKeyException != null) {
                throw lastKeyException;
            } else {
                throw lastKeySpecException;
            }
        }

        private static PrivateKey loadPkcs8EncodedPrivateKey(PKCS8EncodedKeySpec spec) throws InvalidKeySpecException, NoSuchAlgorithmException {
            try {
                return KeyFactory.getInstance("RSA").generatePrivate(spec);
            } catch (InvalidKeySpecException var4) {
                try {
                    return KeyFactory.getInstance("EC").generatePrivate(spec);
                } catch (InvalidKeySpecException var3) {
                    try {
                        return KeyFactory.getInstance("DSA").generatePrivate(spec);
                    } catch (InvalidKeySpecException var2) {
                        throw new InvalidKeySpecException("Not an RSA, EC, or DSA private key");
                    }
                }
            }
        }
    }

    private static class ProviderInstallSpec {
        String className;
        String constructorParam;
        Integer position;

        private ProviderInstallSpec() {
        }

        private boolean isEmpty() {
            return this.className == null && this.constructorParam == null && this.position == null;
        }

        private void installProvider() throws Exception {
            if (this.className == null) {
                throw new com.android.apksigner.ApkSignerTool2.ParameterException("JCA Provider class name (--provider-class) must be specified");
            } else {
                Class<?> providerClass = Class.forName(this.className);
                if (!Provider.class.isAssignableFrom(providerClass)) {
                    throw new com.android.apksigner.ApkSignerTool2.ParameterException("JCA Provider class " + providerClass + " not subclass of " + Provider.class.getName());
                } else {
                    Provider provider;
                    if (this.constructorParam != null) {
                        provider = (Provider)providerClass.getConstructor(String.class).newInstance(this.constructorParam);
                    } else {
                        provider = (Provider)providerClass.getConstructor().newInstance();
                    }

                    if (this.position == null) {
                        Security.addProvider(provider);
                    } else {
                        Security.insertProviderAt(provider, this.position);
                    }

                }
            }
        }
    }
}
