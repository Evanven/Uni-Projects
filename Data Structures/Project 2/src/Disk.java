public class Disk implements Comparable<Disk>
{
    private static int serial = 0;
    public int id;
    public int used_space;
    private int total_space;

    List<Integer> folders;

    public Disk()
    {
        id = serial;
        serial+=1;

        total_space = 1000000;
        used_space = 0;
        folders = new List<>();
    }

    public int getFreeSpace()
    {
        return total_space - used_space;
    }

    @Override
    public int compareTo(Disk other) {
        if(getFreeSpace() > other.getFreeSpace()){
            return 1;
        }
        else if(getFreeSpace() < other.getFreeSpace()) {
            return -1;
        }
        else{
            return 0;
        }
    }

    @Override
    public String toString() {
        return "id "+id+" "+getFreeSpace()+": "+folders.toString();
    }
}
