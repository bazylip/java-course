package DiskTree;

import java.util.Comparator;

public class AlphabeticalComparator implements Comparator<DiskElement>{


    @Override
    public int compare(DiskElement o1, DiskElement o2) {
        if ((o1 instanceof DiskDirectory) && (o2 instanceof DiskFile)){
            return 1;
        }else if ((o1 instanceof DiskFile) && (o2 instanceof DiskDirectory)){
            return -1;
        }else{
            return o1.getName().compareTo(o2.getName());
        }
    }

}
