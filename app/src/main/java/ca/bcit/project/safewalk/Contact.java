package ca.bcit.project.safewalk;

public class Contact {
    private int _id;
    private String _name;
    private String _type;
    private String _phoneNumber;

    public static final Contact[] phoneCall = {
            new Contact("POLICE（EMERGENCY）", "EMERGENCY", "911"),
            new Contact("Hospital(Royal Columbian Hospital)", "EMERGENCY", "6045204253"),
            new Contact("POLICE（NON-EMERGENCY）", "NON-EMERGENCY", "6045255411"),
            new Contact("Taxi (Bel-Air Taxi)", "NON-EMERGENCY", "6044336666"),
    };

    public Contact(String name, String type, String phoneNumber) {
        _type = type;
        _name = name;
        _phoneNumber = phoneNumber;
    }

    public int getId() {return _id; }
    public String getType() {
        return _type;
    }
    public String getName() {
        return _name;
    }
    public String getPhoneNumber() { return _phoneNumber; }
    public String toString() {
        return _name;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_phoneNumber(String _phoneNumber) {
        this._phoneNumber = _phoneNumber;
    }
}
