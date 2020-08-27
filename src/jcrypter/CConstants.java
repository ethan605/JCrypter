package jcrypter;

public class CConstants {
  /*
   * CCrypter constants
   */
  public static final String CRYPT_ALGORITHMS[] = {"AES", "Blowfish", "DES", "DESede"};
  public static final String MESSAGE_DIGEST_ALGORITHMS[] = {"MD2", "MD5", "SHA-1", "SHA-256", "SHA-512"};
  public static final String CHECKSUM_ALGORITHMS[] = {"MD2", "MD5", "SHA-1", "SHA-256", "SHA-512", "CRC32", "Adler32"};
  public static final String CRYPT_MODE = "ECB";
  public static final String CRYPT_PADDING = "PKCS5Padding";
  public static final int DEFAULT_BYTE_PADDED = 0xffff;
  public static final int DEFAULT_CHECKSUM_BYTE = 1000000;
  
  /*
   * CFileCrypter constants
   */
  public static final int CLEAR_BUFFER_ALLOCATION = 99999;
  public static final int ENCRYPTED_BUFFER_ALLOCATION = 100000;
  public static final int FILE_HEALTHY = 0;
  public static final int FILE_CORRUPTED = 1;
  public static final int DEF_FILE_CHECKSUM_LENGTH = 20;
  public static final int DEF_FILE_HEADER_LENGTH = 5;
  public static final int NO_CLEAN_INPUT_FILE_MODE = 0;
  public static final int REMOVE_INPUT_FILE_MODE = 1;
  public static final int WIPE_INPUT_FILE_MODE = 2;
  public static final int DEF_WIPE_INPUT_FILE_TIMES = 10;
  public static final String DEF_FILE_CHECKSUM_ALGORITHM = "SHA-1";
  public static final String DEF_ENCRYPTED_EXTENSION = ".jcr";
  
  /*
   * CFileEncryptThread constants
   */
  public static final String FET_ENCRYPTING_STATUS = "Encrypting file... (";
  public static final String FET_ENCRYPTING_DONE_STATUS = "File encrypted successfully";
  public static final String FET_WIPING_FILE_STATUS = "Wiping input file...";
  public static final String FET_WIPING_FILE_DONE_STATUS = "Wiping input file done";
  
  /*
   * CFileDecryptThread constants
   */
  public static final String FDT_DECRYPTING_STATUS = "Decrypting file... (";
  public static final String FDT_DECRYPTING_DONE_STATUS = "File decrypted successfully";
  
  /*
   * CPasswordInputDialog constants
   */
  public static final String DLG_NEW_PASSWORD_TITLE = "Enter your password";
  public static final String DLG_AUTHENTICATE_PASSWORD_TITLE = "Password authenticate to decrypt file";
  public static final String DLG_ABOUT_TITLE = "About...";
  
  public static final char DEF_PASSWORD_CHAR = '\u25CF';
  public static final String DLG_LBL_PASSWORD_CAPTION = "Password";
  public static final String DLG_LBL_PASSWORD_CONFIRM_CAPTION = "Confirm password";
  public static final String DLG_BTN_OK_CAPTION = "OK";
  public static final String DLG_BTN_CANCEL_CAPTION = "Cancel";
  
  public static final char DLG_LBL_PASSWORD_MNEMONIC = 'P';
  public static final char DLG_LBL_PASSWORD_CONFIRM_MNEMONIC = 'O';
  public static final char DLG_BTN_OK_MNEMONIC = 'O';
  public static final char DLG_BTN_CANCEL_MNEMONIC = 'C';
  public static final String DLG_TXT_INFORMATION_CONTENT = "<html><h2>JCrypter</h2>" +
                                "<h5>version 1.5</h5><br>" +
                                "JCrypter is a utility to encrypt/decrypt files and texts<br>" +
                                "using variant Cryptographic, Message Digest & Checksum algorithms<br>" +
                                "This application was developed in pure Java using Eclipse Helios IDE<br>" +
                                "<br><center><i>Copyright (c) 2011, Atlas<i></center></html>";
  
  public static final String DLG_PASSWORD_INPUT_CANCEL_INFO_TITLE = "Password canceled";
  public static final String DLG_PASSWORD_INPUT_CANCEL_INFO_CONTENT = "You've canceled password confirmation\nYour file will be encrypted without password protection";
  public static final String DLG_PASSWORD_AUTHENTICATE_CANCEL_INFO_TITLE = "Password canceled";
  public static final String DLG_PASSWORD_AUTHENTICATE_CANCEL_INFO_CONTENT = "You've canceled inputing password\nYour file won't be decrypted";
  public static final String DLG_PASSWORD_BLANK_ERROR_TITLE = "Error!";
  public static final String DLG_PASSWORD_BLANK_ERROR_CONTENT = "Password must not be blank!";
  public static final String DLG_PASSWORD_NOT_MATCH_ERROR_TITLE = "Error!";
  public static final String DLG_PASSWORD_NOT_MATCH_ERROR_CONTENT = "Password must be match with confirm password";
  public static final String DLG_PASSWORD_MATCH_INFO_TITLE = "Password confirmed";
  public static final String DLG_PASSWORD_MATCH_INFO_CONTENT = "Password match!\nYour file will be encrypted now";
  public static final String DLG_PASSWORD_AUTHENTICATED_INFO_TITLE = "Password match";
  public static final String DLG_PASSWORD_AUTHENTICATED_INFO_CONTENT = "Authenticated!\nYour file will be decrypted now";
  public static final String DLG_PASSWORD_WRONG_ERROR_TITLE = "Wrong password";
  public static final String DLG_PASSWORD_WRONG_ERROR_CONTENT = "Wrong password!\nPlease try again";
  
  /*
   * CTextCryptThread constants
   */
  public static final String TCT_CANNOT_DECRYPT_TEXT_ERROR_TITLE = "Decrypt failed";
  public static final String TCT_CANNOT_DECRYPT_TEXT_ERROR_CONTENT = "Can not decrypt the message text";
  
  /*
   * JCrypter constants
   */
  protected static final int DEF_SOCK_PORT = 12345;
  protected static final byte DEF_LOCAL_HOST[] = {127, 0, 0, 1};
  public static final String DEF_INTERFACE_FONT = "Tahoma";
  public static final String DEF_TEXT_CODE_FONT = "Courier New";
  protected static final String MAIN_TITLE = "JCrypter v1.5";
  protected static final String EXISTED_INSTANCE_WARNING_TITLE = "Warning!";
  protected static final String EXISTED_INSTANCE_WARNING_CONTENT = "An existed program has been started!";
  
  /*
   * CMainMenu constants
   */
  protected static final String MNU_JCrypter_CAPTION = "JCrypter";
  protected static final String MNU_JCrypter_EXIT_CAPTION = "Exit";
  protected static final String MNU_HELP_CAPTION = "Help";
  protected static final String MNU_HELP_ABOUT_CAPTION = "About";
  
  protected static final char MNU_JCRYPTER_MNEMONIC = 'J';
  protected static final char MNU_JCRYPTER_EXIT_MNEMONIC = 'X';
  protected static final char MNU_HELP_MNEMONIC = 'H';
  protected static final char MNU_HELP_ABOUT_MNEMONIC = 'A';
  
  protected static final String MNU_JCRYPTER_EXIT_KEYSTROKE = "control Q";
  protected static final String MNU_HELP_ABOUT_KEYSTROKE = "F1";
  
  /*
   * CFileCrypterPanel constants
   */
  protected static final String FILE_CRYPTER_PANEL_TITLE = "File crypter";
  protected static final String FCR_TXT_INPUT_CAPTION = "Input file";
  protected static final String FCR_TXT_OUTPUT_CAPTION = "Output file";
  protected static final String FCR_BTN_BROWSE_CAPTION = "Browse";
  protected static final String FCR_LBL_CRYPT_ALGORITHM_CAPTION = "Encrypt method";
  protected static final String FCR_LBL_MESSAGE_DIGEST_ALGORITHM_CAPTION = "Password protection";
  protected static final String FCR_CKB_REMOVE_INPUT_CAPTION = "Remove input file after operations";
  protected static final String FCR_CKB_WIPEOUT_INPUT_CAPTION = "Wipe input file after operations";
  protected static final String FCR_BTN_ENCRYPT_CAPTION = "Encrypt!";
  protected static final String FCR_BTN_DECRYPT_CAPTION = "Decrypt!";
  protected static final String FCR_PGB_CHECKING_FILE_CONTENT = "Checking file...";
  protected static final String FCR_PGB_CHECKING_FILE_DONE_CONTENT = "Checking file done!";
  
  protected static final char FILE_CRYPTER_PANEL_MNEMONIC = 'F';
  protected static final char FCR_LBL_INPUT_MNEMONIC = 'I';
  protected static final char FCR_BTN_BROWSE_INPUT_MNEMONIC = 'B';
  protected static final char FCR_BTN_BROWSE_OUTPUT_MNEMONIC = 'O';
  protected static final char FCR_LBL_CRYPT_ALGORITHM_MNEMONIC = 'M';
  protected static final char FCR_LBL_MESSAGE_DIGEST_ALGORITHM_MNEMONIC = 'P';
  protected static final char FCR_CKB_REMOVE_INPUT_MNEMONIC = 'R';
  protected static final char FCR_CKB_WIPEOUT_INPUT_MNEMONIC = 'W';
  protected static final char FCR_BTN_ENCRYPT_MNEMONIC = 'E';
  protected static final char FCR_BTN_DECRYPT_MNEMONIC = 'D';
  
  protected static final String FCR_TXT_INPUT_TOOLTIP = "Input a file to encrypt/decrypt";
  protected static final String FCR_TXT_OUTPUT_TOOLTIP = "Your encrypted/decrypted filepath";
  protected static final String FCR_BTN_BROWSE_INPUT_TOOLTIP = "Browse for input file";
  protected static final String FCR_BTN_BROWSE_OUTPUT_TOOLTIP = "Browse for output folder";
  protected static final String FCR_BTN_ENCRYPT_TOOLTIP = "Encrypt your input file";
  protected static final String FCR_BTN_DECRYPT_TOOLTIP = "Decrypt your input file";
  
  /*
   * CFileChecksumPanel constants
   */
  protected static final String FILE_CHECKSUM_PANEL_TITLE = "File checksum";
  protected static final String FCS_TXT_INPUT_CAPTION = "Input file";
  protected static final String FCS_BTN_BROWSE_INPUT_CAPTION = "Browse";
  protected static final String FCS_LBL_CHECKSUM_ALGORITHM_CAPTION = "Checksum method";
  protected static final String FCS_LBL_CHECKSUM_RESULT_CAPTION = "File checksum result";
  protected static final String FCS_CKB_UPPER_CASE_DISPLAY_CAPTION = "Use upper cases";
  protected static final String FCS_BTN_CHECKSUM_CAPTION = "Checksum!";
  
  protected static final char FILE_CHECKSUM_PANEL_MNEMONIC = 'C';
  protected static final char FCS_TXT_INPUT_MNEMONIC = 'I';
  protected static final char FCS_BTN_BROWSE_INPUT_MNEMONIC = 'B';
  protected static final char FCS_LBL_CHECKSUM_ALGORITHM_MNEMONIC = 'M';
  protected static final char FCS_LBL_CHECKSUM_RESULT_MNEMONIC = 'R';
  protected static final char FCS_CKB_UPPER_CASE_DISPLAY_MNEMONIC = 'U';
  protected static final char FCS_BTN_CHECKSUM_MNEMONIC = 'E';
  
  protected static final String FCS_TXT_INPUT_TOOLTIP = "Input a file to get checksum";
  protected static final String FCS_BTN_BROWSE_INPUT_TOOLTIP = "Browse for input file";
  protected static final String FCS_BTN_CHECKSUM_TOOLTIP = "Get file checksum";
  protected static final String COPIED_TO_CLIPBOARD_TOOLTIP = "Copied to clipboard";
  
  /*
   * CTextCrypterPanel constants
   */
  protected static final String TEXT_CRYPTER_PANEL_TITLE = "Text crypter";
  protected static final String TCR_LBL_KEY_WORD_CAPTION = "Key word";
  protected static final String TCR_LBL_PLAIN_TEXT_CAPTION = "<html><u>P</u>lain<br>text</html>";
  protected static final String TCR_LBL_CRYPTED_TEXT_CAPTION = "<html>C<u>r</u>ypted<br>text</html>";
  protected static final String TCR_BTN_CRYPT_CAPTION = "Crypt";
  protected static final String TCR_LBL_CRYPT_ALGORITHM_CAPTION = "Crypt method";
  
  protected static final char TEXT_CRYPTER_PANEL_MNEMONIC = 'T';
  protected static final char TCR_LBL_KEY_WORD_MNEMONIC = 'K';
  protected static final char TCR_LBL_PLAIN_TEXT_MNEMONIC = 'P';
  protected static final char TCR_LBL_CRYPTED_TEXT_MNEMONIC = 'R';
  protected static final char TCR_BTN_CRYPT_MNEMONIC = 'Y';
  protected static final char TCR_LBL_CRYPT_ALGORITHM_MNEMONIC = 'M';
  
  /*
   * CTextChecksumPanel constants
   */
  protected static final String TEXT_CHECKSUM_PANEL_TITLE = "Text checksum";
  protected static final String TCS_LBL_INPUT_TEXT_CAPTION = "Input";
  protected static final String TCS_LBL_CHECKSUM_RESULT_CAPTION = "Checksum";
  protected static final String TCS_LBL_CHECKSUM_ALGORITHM_CAPTION = "Checksum method";
  protected static final String TCS_CKB_UPPER_CASE_DISPLAY_CAPTION = "Use upper cases";
  protected static final String TCS_BTN_CLEAR_CAPTION = "Clear";
  
  protected static final char TEXT_CHECKSUM_PANEL_MNEMONIC = 'S';
  protected static final char TCS_LBL_INPUT_TEXT_MNEMONIC = 'I';
  protected static final char TCS_LBL_CHECKSUM_RESULT_MNEMONIC = 'E';
  protected static final char TCS_LBL_CHECKSUM_ALGORITHM_MNEMONIC = 'M';
  protected static final char TCS_CKB_UPPER_CASE_DISPLAY_MNEMONIC = 'U';
  protected static final char TCS_BTN_CLEAR_MNEMONIC = 'R';
}
