package woy.woy.woy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Node implements Comparable<Node>{
    private int id;
    private long modTime;

    @Override
    public int compareTo(Node node) {
        return (int) (node.getModTime() - this.getModTime());
    }
}
