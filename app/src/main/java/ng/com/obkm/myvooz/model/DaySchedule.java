package ng.com.obkm.myvooz.model;


import android.os.Parcel;
import android.os.Parcelable;

public class DaySchedule  implements Parcelable {

    private String name;
    private String classroom;
    private int type;
    private int id_type;
    private String name_teacher;
    private String name_type;
    private int id;
    private String weeks;
    private String groups;
    private int id_object;
    private Integer number;
    private Integer wday;
    private Integer min_week;
    private Integer max_week;
    private boolean state;
    private String time_f;
    private String time_l;
    private boolean is_run;

    public DaySchedule(Parcel in) {
    }

    public static final Creator<DaySchedule> CREATOR = new Creator<DaySchedule>() {
        @Override
        public DaySchedule createFromParcel(Parcel in) {
            return new DaySchedule(in);
        }

        @Override
        public DaySchedule[] newArray(int size) {
            return new DaySchedule[size];
        }
    };

    public DaySchedule(Integer id, String name_object, String classroom, String name_type, String name_teacher, Integer number, String time_f, String time_l, Integer wday, Integer min_week, Integer max_week) {
        this.id = id;
        this.name = name_object;
        this.classroom = classroom;
        this.name_type = name_type;
        this.name_teacher = name_teacher;
        this.number = number;
        this.time_f = time_f;
        this.time_l = time_l;
        this.max_week = max_week;
        this.min_week = min_week;
        this.wday = wday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getType() {
        return type;
    }

    public int getWday() {
        return wday;
    }

    public int getMinWeek() {
        return min_week;
    }

    public int getMaxWeek() {
        return max_week;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    public String getName_teacher() {
        return name_teacher;
    }

    public void setName_teacher(String name_teacher) {
        this.name_teacher = name_teacher;
    }

    public String getNameType() {
        return name_type;
    }

    public void setNameType(String name_type) {
        this.name_type = name_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public int getId_object() {
        return id_object;
    }

    public void setId_object(int id_object) {
        this.id_object = id_object;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getTimeF() {
        return time_f;
    }

    public void setTimeF(String time) {
        this.time_f = time;
    }

    public String getTimeL() {
        return time_l;
    }

    public void setTimeL(String time) {
        this.time_l = time;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}