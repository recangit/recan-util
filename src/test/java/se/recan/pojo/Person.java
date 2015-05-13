package se.recan.pojo;

import java.io.Serializable;
import org.jdom2.Attribute;
import org.jdom2.Element;

/**
 *
 * 2014-aug-06
 * @author Anders Recksén (recan)
 */
public class Person implements Serializable {

    private int id = -1;
    private String firstName = "";
    private String lastName = "";
    private String userName = "";
    private String password = "";
    private String password2 = "";
    private String socialNumb = "";
    private int age = -1;
    private int gender = -1;
    private boolean exist;
    
    public Person() {}
    
    public Person(String firstName) {
        this.firstName = firstName;
    }
    
    public Person(int id) {
        this.id = id;
    }
    
    public Person(int id, String firstName, String lastName, String userName, String password, String password2, String socialNumb, int age, int gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.password2 = password2;
        this.socialNumb = socialNumb;
        this.age = age;
        this.gender = gender;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getPassword2() {
        return password2;
    }

    public void setSocialNumb(String socialNumb) {
        this.socialNumb = socialNumb;
    }

    public void setSocialNumb(long socialNumb) {
        try {
            this.socialNumb = Long.toString(socialNumb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSocialNumb() {
        return socialNumb;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getGender() {
        return gender;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public int getAge() {
        return age;
    }

    public Element toXml() {
        Element element = new Element("person");
        element.setAttribute(new Attribute("id", Integer.toString(this.id)));

        Element e = new Element("firstName");
        e.setAttribute(new Attribute("value", this.firstName));
        element.addContent(e);

        e = new Element("lastName");
        e.setAttribute(new Attribute("value", this.lastName));
        element.addContent(e);

        e = new Element("userName");
        e.setAttribute(new Attribute("value", this.userName));
        element.addContent(e);

        e = new Element("password");
        e.setAttribute(new Attribute("value", this.password));
        element.addContent(e);

        e = new Element("password2");
        e.setAttribute(new Attribute("value", this.password2));
        element.addContent(e);

        e = new Element("socialNumb");
        e.setAttribute(new Attribute("value", this.socialNumb));
        element.addContent(e);
        
        e = new Element("age");
        e.setAttribute(new Attribute("value", String.valueOf(this.age)));
        element.addContent(e);

        e = new Element("gender");
        e.setAttribute(new Attribute("value", this.gender == 0 ? "0" : "1"));
        element.addContent(e);

        return element;
    }
    
    public void setExist(boolean exist) { this.exist = exist; }

    public boolean isExist() { return exist; }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("\n");
        builder.append(this.id);
        builder.append("\n");
        builder.append(this.firstName);
        builder.append("\n");
        builder.append(this.lastName);
        builder.append("\n");
        builder.append(this.userName);
        builder.append("\n");
        builder.append(this.password);
        builder.append("\n");
        builder.append(this.password2);
        builder.append("\n");
        builder.append(this.socialNumb);
        builder.append("\n");
        builder.append(this.gender);
        builder.append("\n");
        builder.append(this.age);
        builder.append("\n");

        return builder.toString();
    }
}