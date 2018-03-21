//@author JR515

import java.util.List;
import java.util.ArrayList;

public class Pack
{
    private String name;
    private String version;
    private Long size;
    private List<List<String>> depends = new ArrayList<>();
    private List<String> conflicts = new ArrayList<>();

    public String getName() { return name; }
    public String getVersion() { return version; }
    public String getSize() { String s =Long.toString(size); return s;}
    public List<List<String>> getDepends() { return depends; }
    public List<String> getConflicts() { return conflicts; }
    public void setName(String name) { this.name = name; }
    public void setVersion(String version) { this.version = version; }
    public void setSize(Long size) { this.size = size; }
    public void setDepends(List<List<String>> depends) { this.depends = depends; }
    public void setConflicts(List<String> conflicts) { this.conflicts = conflicts; } 

    @Override
		public String toString() {
      return String.format("\"+%s=%s\"", name, version);
    }     

}
