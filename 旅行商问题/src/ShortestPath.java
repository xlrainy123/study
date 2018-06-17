import java.util.*;


public class ShortestPath {
    //记录路径
    int[][] path = null;
    HashMap<Character, Integer> chIntMap = null;
    //记录状态列点表
    PriorityQueue<Node> nodeQueue = null;
    //构造比较函数
    Comparator<Node> comparator = new Comparator<Node>() {
        public int compare(Node o1, Node o2) {
            return o1.getLb() - o2.getLb();
        }
    };
    public ShortestPath(int[][] path, HashMap<Character, Integer> chIntMap) {
        this.path = path;
        this.chIntMap = chIntMap;
        this.nodeQueue = new PriorityQueue<Node>(comparator);
    }
    public static void main(String[] args) {
        //点
        char[] chs = {'a', 'b', 'c', 'd', 'e'};
        HashMap<Character, Integer> chIntMap = new HashMap<Character, Integer>();
        for (int i = 0; i < chs.length; i++) {
            chIntMap.put(chs[i], i);
        }
        int[][] path = {{0, 3, 1, 5, 8}, {3, 0, 6, 7, 9},
                        {1, 6, 0, 4, 2}, {5, 7, 4, 0, 3},
                        {8, 9, 2, 3, 0}};
        ShortestPath sp = new ShortestPath(path, chIntMap);
        ArrayList<Integer> dotsList = new ArrayList<Integer>();
        dotsList.add(0);
        HashMap<Integer, ArrayList<Line>> linesMap = sp.shortestEdge();
        Node root = new Node(0, 0);
        root.setLines(linesMap);
        root.setDots(dotsList);
        Node bestNode = sp.bestPath(root);
        System.out.println(bestNode.getL());
        for (Integer i : bestNode.getDots()) {
            System.out.print(chs[i] + " ");
        }
    }

    /**
     * 最优路径
     *
     * @param root
     * @return
     */
    public Node bestPath(Node root) {
        //将开始节点加入队列
        nodeQueue.add(root);
        //记录最优结果
        Node bestNode = root;
        int bestL = Integer.MAX_VALUE;
        //记录当前操作节点
        Node currentNode = null;
        while (!nodeQueue.isEmpty()) {
            currentNode = nodeQueue.poll();
            int index = currentNode.getIndex();
            //获取当前节点所在点的相邻点
            ArrayList<Integer> adjacentPoints = getAdjacentPoints(index);
            for (int i : adjacentPoints) {
                //当前已被选择点列表
                ArrayList<Integer> dotsList = currentNode.getDots();
                if (!dotsList.contains(i)) {
                    //新增节点后所包含的路径
                    ArrayList<Integer> childDotsList = new ArrayList<Integer>();
                    for (int dot : dotsList) {
                        childDotsList.add(dot);
                    }
                    childDotsList.add(i);
                    //判断扩展后是否满足条件b在c前面
                    if (judgeBCconstraint(childDotsList)) {
                        int L = currentNode.getL() + path[index][i];
                        Line middleLine = new Line(index, i, path[index][i]);
                        Node childNode = new Node(L, i);
                        childNode.setDots(childDotsList);
                        //该状态点计算lb边界的线段
                        HashMap<Integer, ArrayList<Line>> linesMap = new HashMap<Integer, ArrayList<Line>>();
                        linesMap = copyHashMap(currentNode.getLines());
                        childNode.setLines(linesMap);
                        //计算新增点后边界
                        childNode.setLb(childNode.computeLb(middleLine));
                        if (childDotsList.size() < path.length) {
                            //有机会成为更优的路径，则加入优先队列
                            if (childNode.getLb() < bestL) {
                                nodeQueue.add(childNode);
                            }
                        } else {
                            //获取开始点
                            int start = childNode.getDots().get(0);
                            int cost = childNode.getL() + path[i][start];
                            //加入开始节点，作为终点
                            childNode.getDots().add(start);
                            //已经找到一条路径
                            if (bestL > cost) {
                                bestL = cost;
                                childNode.setL(cost);
                                bestNode = childNode;
                            }
                        }
                    }
                }
            }
        }
        //将最优路径返回
        return bestNode;
    }

    /**
     * 复制Map中的内容
     * @param lines
     * @return
     */
    public HashMap<Integer, ArrayList<Line>> copyHashMap(HashMap<Integer, ArrayList<Line>> lines) {

        HashMap<Integer, ArrayList<Line>> linesMap = new HashMap<Integer, ArrayList<Line>>();

        for (Map.Entry<Integer, ArrayList<Line>> entry : lines.entrySet()) {

            ArrayList<Line> linesList = new ArrayList<Line>();

            for (Line l : entry.getValue()) {

                Line tempLine = new Line(l.getStart(), l.getEnd(), l.getLength());

                linesList.add(tempLine);

            }

            linesMap.put(entry.getKey(), linesList);

        }

        return linesMap;

    }

    /**
     * 返回选定点的所有相邻点
     *
     * @param start
     * @return
     */
    public ArrayList<Integer> getAdjacentPoints(int start) {
        //存储相邻节点
        ArrayList<Integer> adjacentPoints = new ArrayList<Integer>();
        int[] arr = path[start];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 0) {
                adjacentPoints.add(i);
            }
        }
        return adjacentPoints;
    }
    /**
     * 获取所给图的每个点的最短两条边
     *
     * @return
     */
    public HashMap<Integer, ArrayList<Line>> shortestEdge() {
        //保存每个节点的最短边
        HashMap<Integer, ArrayList<Line>> linesMap = new HashMap<Integer, ArrayList<Line>>();
        for (int i = 0; i < path.length; i++) {
            int min = Integer.MAX_VALUE;
            //保存第一个最近的点
            int firstDot = -1;
            //保存第二个较近的点
            int secondDot = -1;
            //存储一个点的最近两条边
            ArrayList<Line> linesList = new ArrayList<Line>();
            for (int j = 0; j < path[i].length; j++) {
                if (i == j) {
                    continue;
                }
                if (path[i][j] > 0 && min > path[i][j]) {
                    min = path[i][j];
                    firstDot = j;
                }
            }
            min = Integer.MAX_VALUE;
            Line line = new Line(i, firstDot, path[i][firstDot]);
            linesList.add(line);
            //寻找第二个最近点
            for (int j = 0; j < path[i].length; j++) {
                if (i == j || j == firstDot) {
                    continue;
                }
                if (path[i][j] > 0 && min > path[i][j]) {
                    min = path[i][j];
                    secondDot = j;
                }
            }
            Line secondLine = new Line(i, secondDot, path[i][secondDot]);
            linesList.add(secondLine);
            linesMap.put(i, linesList);
        }
        return linesMap;
    }
    /**
     * 判断b在c之前的约束是否满足
     *
     * @param dots
     * @return
     */
    public boolean judgeBCconstraint(ArrayList<Integer> dots) {
        //计算字符b对应数值
        int bNum = chIntMap.get('b');
        //计算字符c对应数值
        int cNum = chIntMap.get('c');
        //记录节点b在列表的下标位置
        int bIndex = -2;
        //计算节点c在列表中的下标位置
        int cIndex = -1;
        for (int i = 0; i < dots.size(); i++) {
            if (dots.get(i) == bNum) {
                bIndex = i;
            }
            if (dots.get(i) == cNum) {
                cIndex = i;
            }
        }
        if (cIndex < 0) {
            return true;
        } else {
            if (bIndex < 0) {
                return false;
            } else if (bIndex > cIndex) {
                return false;
            } else {
                return true;
            }
        }
    }
}

