import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Programming Assignment Number 1
 * @author Brandon Bautista
 * Section: 02
 */
public class CaesarCipher
{
  private static final int SHIFT_MAX = 26;

  /**
   * encrypt will turn a plaintext message into ciphertext based on the amount specified in the
   * shift parameter.
   * 
   * @param plain
   *          the plaintext to be encrypted
   * @param shift
   *          the amount of spaces to shift
   * @return the resulting ciphertext
   */
  public static String encrypt(String plain, int shift)
  {
    String encryption = "";
    char character; 
    char[] plainCharacters = plain.toLowerCase().toCharArray();

    for (int i = 0; i < plainCharacters.length; i++)
    {     

      character = (char) (plainCharacters[i] + shift);

      //If the shift causes the character to be an ASCII character greater than z..
      if (character > 'z')
        encryption += (char) (plainCharacters[i] - (SHIFT_MAX - shift));  

      else if (character < 'a')
        encryption += (char) (plainCharacters[i] + (SHIFT_MAX + shift));

      else
        encryption += character;

    }
    return encryption.toUpperCase();
  }

  /**
   * decrypt takes a ciphertext message and caesar shifts it according to its shift parameter to
   * return the resulting plaintext.
   * 
   * @param cipher
   *          the ciphertext to be decrypted
   * @param shift
   *          the amount of spaces to shift
   * @return the resulting plaintext
   */
  public static String decrypt(String cipher, int shift)
  {
    String plainText = "";
    char character;
    char[] cipherChars = cipher.toUpperCase().toCharArray();

    for (int i = 0; i < cipherChars.length; i++)
    {
      character = (char) (cipherChars[i] - shift);

      if (character < 'A')
        plainText += (char) (cipherChars[i] + (SHIFT_MAX - shift));

      else if (character > 'Z')
        plainText += (char) (cipherChars[i] - (SHIFT_MAX + shift));

      else
        plainText += character;
    }

    return plainText;
  }

  /**
   * validMessage checks if a message is a string that is composed of only English letters.
   * 
   * @param message
   *          the message to check
   * @return the boolean value of whether the message is valid or not
   */
  public static boolean validMessage(String message)
  {
    char[] messageCharacters = null;
    messageCharacters = message.toCharArray();

    for (int i = 0; i < messageCharacters.length; i++)
    {
      if (!Character.isLetter(messageCharacters[i]))
      {
        System.out.println("You entered: " + message);
        System.out.printf("'%c' is not a letter. It was in position %d of the message.\n",
            messageCharacters[i], i);
        return false;
      }
    }
    return true;
  }
  
  /**
   * validShift checks if a shift is within the valid range
   * 
   * @param shift amount to shift by
   * @return the boolean value of whether the shift is valid or not
   */
  public static boolean validShift(int shift)
  {
    if (shift < -26 || shift > 26)
    {
      System.out.println(shift + " is not in the valid range of [-26, 26], please try again.");
      return false;
    }
    return true;
  }

  public static void main(String[] args)
  {
    
    int shift = 0; // amount of spaces to shift by
    char mode; // E or D
    char typeOfMessage; // ask for an input of a string or a text file
    Scanner scan = new Scanner(System.in);
    String message = null;
    String returnedMessage = null; // the resulting message to be printed
    String fileName = null; 
    String filePath = null;
    String shiftHolder = null; // used for validation of shift
    String originalMessage = null; // the valid message entered w/o removing blanks

    /* TODO Add a GUI interface with file explorer to find file to encrypt/decrypt*/
    
    System.out.println("Do you want to encrypt (E) or decrypt (D)?");
    mode = scan.next().trim().charAt(0);

    System.out.println("Do you want to type in the message (T) or read from a text file (F)?");
    typeOfMessage = scan.next().trim().charAt(0);
    scan.nextLine();

    if (typeOfMessage == 'T')
    {
      /* Get the message from the user and perform error checking */
      do
      {
        if (mode == 'E')
          System.out.println("Please type the message you want encrypted");
        else
          System.out.println("Please type the message you want decrypted");
        
        message = scan.nextLine();
        originalMessage = message; //to hold the message before deleting the blanks
        if (mode == 'E')
          message = message.replaceAll("\\s+", "");
      }
      while (!validMessage(message));

      /* Get the shift from the user and perform error checking */
      do
      {
        System.out.println("By how many spaces would you like to shift?");
        /* Remove any blanks in the input for shifts */
        shiftHolder = scan.nextLine();
        shiftHolder = shiftHolder.replaceAll("\\s+", "");
        shift = Integer.parseInt(shiftHolder);
      }
      while (!validShift(shift));
    
    }
 
    /* Get message and shift from a specified file */
    if (typeOfMessage == 'F')
    {

      do
      {
        System.out.println("Enter the name of a file with a valid message and shift:");
        fileName = scan.nextLine();
        filePath = "/Users/brandorian/Desktop/"; //change accordingly to where file is stored
        
        /* Attempt to start reading from the file specified by the user */
        try
        {
          BufferedReader reader = new BufferedReader(new FileReader(filePath + fileName));
          String line;
          List<String> lines = new ArrayList<String>();
          try
          {
            while ((line = reader.readLine()) != null)
            {
              lines.add(line);
            }
            reader.close();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }

          String[] data = lines.toArray(new String[] {});
          message = data[0];
          originalMessage = message; //To hold the message before deleting blanks
          if (mode == 'E')
            message = message.replaceAll("\\s+", "");
          shift = Integer.parseInt(data[1]);

        }
        catch (FileNotFoundException e)
        {
          e.printStackTrace();
        } 
      }
      while (!validMessage(message) || !validShift(shift));
    }

    /* If a user wants to encrypt */
    if (mode == 'E')
    {
      System.out.println("The plaintext message is:  " + originalMessage);
      returnedMessage = encrypt(message, shift);
      System.out.println("The encrypted message is:  " + returnedMessage);
    }

    /* If a user wants to decrypt */
    if (mode == 'D')
    {
      System.out.println("The encrypted message is:  " + message);
      returnedMessage = decrypt(message, shift);
      System.out.println("The plaintext message is:  " + returnedMessage);
    }
    scan.close();
  }
}
