package ch.eonum.grouperserve;

import static spark.Spark.*;

public class GrouperServe {
	public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
    }
}
