package bcit.ca.safewalkfragments;

public class Contact {
    private String _type;
    private String _name;
    private String _phoneNumber;

    public static final Contact[] phoneCall = {
            new Contact("EMERGENCY", "POLICE（EMERGENCY）", "911"),
            new Contact("NON-EMERGENCY", "POLICE（NON-EMERGENCY）", "6045255411"),
            new Contact("NON-EMERGENCY", "Test Number", "7788899463"),
    };

    public Contact(String type, String name, String phoneNumber) {
        _type = type;
        _name = name;
        _phoneNumber = phoneNumber;
    }

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
}
