package org.bin2.jag.dao.query.sfm;

/**
 * Created by benoitroger on 29/08/14.
 */
public class MyObject {
    private long id;
    private String email;
    private String myProperty;

    public MyObject(long id, String email, String myProperty) {
        this.id = id;
        this.email = email;
        this.myProperty = myProperty;
    }

    public long getId() {
        return id;
    }

    public String getMyProperty() {
        return myProperty;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MyObject{");
        sb.append("id=").append(id);
        sb.append(", email='").append(email).append('\'');
        sb.append(", myProperty=").append(myProperty);
        sb.append('}');
        return sb.toString();
    }
}
