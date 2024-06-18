package com.example.fxo;

public class User {
    private User() { }
    private static User instance;
    private int userID;
    private int flashcardID;
    private int folderID;
    private int eventID;
    private int contactNo;

    private int flashcardFolderID;
    private String userName;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String folder;
    private String flashcardFolderTitle;
    private String subject;
    private String question;
    private String answer;

    private String eventName;
    private String eventDate;
    private String eventReminder;


    public static synchronized User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public static void setInstance(User instance) {
        User.instance = instance;
    }

    // User Infromation::
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getContactNo() {
        return contactNo;
    }

    public void setContactNo(int contactNo) {
        this.contactNo = contactNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }



    // Folder (Sets) table
    public int getFolderID() {
        return folderID;
    }

    public void setFolderID(int folderID) {
        this.folderID = folderID;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }




    // Flashcard Folder table
    public int getFlashcardFolderID() {
        return flashcardFolderID;
    }

    public void setFlashcardFolderID(int flashcardFolderID) {
        this.flashcardFolderID = flashcardFolderID;
    }

    public String getFlashcardFolderTitle() {
        return flashcardFolderTitle;
    }

    public void setFlashcardFolderTitle(String flashcardFolderTitle) {
        this.flashcardFolderTitle = flashcardFolderTitle;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }



    // Flashcard table
    public int getFlashcardID() {
        return flashcardID;
    }

    public void setFlashcardID(int flashcardID) {
        this.flashcardID = flashcardID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }



    // Event table
    public int getEventID(){return eventID;}
    public String getEventName(){return eventName;}
    public String getEventDate() {return eventDate;}
    public String getEventReminder() {return eventReminder;}
}
