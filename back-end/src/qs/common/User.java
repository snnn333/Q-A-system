package qs.common;

/**
 * 数据库中User实体
 */
public class User{

    private Long id;
    private Long role;
    private Long score;
    private Long age;
    private Long numOfQuery;
    private Long numOfAnswer;
    private Long cannotSpeak;
    private Long cannotLogin;
    private String name;
    private String phone;
    private String mail;
    private String password;
    private String photo;

    public Long getId() { return id; }
    public Long getRole() { return role; }
    public Long getScore() { return score; }
    public Long getAge() { return age; }
    public Long getNumOfQuery() { return numOfQuery; }
    public Long getNumOfAnswer() { return numOfAnswer; }
    public Long getCannotSpeak() { return cannotSpeak; }
    public Long getCannotLogin() { return cannotLogin; }
    public String getName() { return name;}
    public String getPhone() { return phone; }
    public String getmail() { return mail; }
    public String getPassword() { return password; }
    public String getPhoto() { return photo; }

    public void setId(Long id) { this.id = id; }
    public void setRole(Long role) { this.role = role; }
    public void setScore(Long score) { this.score = score; }
    public void setAge(Long age) { this.age = age; }
    public void setNumOfQuery(Long numOfQuery) { this.numOfQuery = numOfQuery; }
    public void setNumOfAnswer(Long numOfAnswer) { this.numOfAnswer = numOfAnswer; }
    public void setCannotSpeak(Long cannotSpeak) { this.cannotSpeak = cannotSpeak; }
    public void setCannotLogin(Long cannotLogin) { this.cannotLogin = cannotLogin; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setMail(String mail) { this.mail = mail; }
    public void setPassword(String password) { this.password = password; }
    public void setPhoto(String photo) { this.photo = photo; }

};
