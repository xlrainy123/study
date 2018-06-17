import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Node {
    //最优边界
    int lb = 0;
    //记录长度
    int l = 0;
    //当前点
    int index = 0;
    //记录长度
    ArrayList<Integer> dots = null;
    //记录计算最小边界的边
    HashMap<Integer, ArrayList<Line>> lines = null;
    public Node() {
        dots = new ArrayList<Integer>();
        lines = new HashMap<Integer, ArrayList<Line>>();
    }
    public Node(int l, int index) {
        this.l = l;
        this.index = index;
        dots = new ArrayList<Integer>();
        lines = new HashMap<Integer, ArrayList<Line>>();
    }
    /**
     * 计算新的lb
     *
     * @param line
     * @return
     */
    public int computeLb(Line line) {
        //替换line.start为起点边
        ArrayList<Line> startLinesList = lines.get(line.getStart());
        boolean isExist = containSameLine(startLinesList, line);
        if (!isExist) {
            int index = getLongestLine(startLinesList);
            startLinesList.set(index, line);
        }
        //替换line.end为起点的边
        ArrayList<Line> endLinesList = lines.get(line.getEnd());
        isExist = containSameLine(endLinesList, line);
        if (!isExist) {
            int index = getLongestLine(endLinesList);
            endLinesList.set(index, line);
        }
        //计算新的Lb
        int lb = 0;
        for (Map.Entry<Integer, ArrayList<Line>> entry : lines.entrySet()) {
            for (Line l : entry.getValue()) {
                lb += l.getLength();
            }
        }
        //向上取整
        lb = lb % 2 == 0 ? (lb / 2) : ((lb + 1) / 2);
        return lb;
    }

    /**
     * 判断边是否包含在集合中
     *
     * @param linesList
     * @param line
     * @return
     */
    public boolean containSameLine(ArrayList<Line> linesList, Line line) {
        for (Line l : linesList) {
            if (l.getStart() == line.getStart()) {
                if (l.getEnd() == line.getEnd()) {
                    return true;
                }
            } else if (l.getStart() == line.getEnd()) {
                if (l.getEnd() == line.getStart()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取最长边在集合中的下标
     *
     * @param linesList
     * @return
     */
    public int getLongestLine(ArrayList<Line> linesList) {
        int index = 0;
        for (int i = 1; i < linesList.size(); i++) {
            if (linesList.get(i).length > linesList.get(index).length) {
                index = i;
            }
        }
        return index;
    }

    public int getLb() {
        return lb;
    }
    public void setLb(int lb) {
        this.lb = lb;
    }
    public int getL() {
        return l;
    }
    public void setL(int l) {
        this.l = l;
    }
    public ArrayList<Integer> getDots() {
        return dots;
    }
    public void setDots(ArrayList<Integer> dots) {
        this.dots = dots;
    }
    public HashMap<Integer, ArrayList<Line>> getLines() {
        return lines;
    }
    public void setLines(HashMap<Integer, ArrayList<Line>> lines) {
        this.lines = lines;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}