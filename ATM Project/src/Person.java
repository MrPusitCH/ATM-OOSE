public class Person {
    private String id;       // รหัสประชาชน
    private String name;     // ชื่อ-นามสกุล
    private String gender;   // เพศ

    public Person(String id, String name, String gender) {
        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }
}
