const PI: f64 = 3.1416;

struct Circle {
    radius: f64
}

struct Rectangle {
    height: f64,
    width: f64
}

trait Shape {
    fn area(&self) -> f64;
}

impl Shape for Circle {
    fn area(&self) -> f64 {
        PI * self.radius * self.radius
    }
}

impl Shape for Rectangle {
    fn area(&self) -> f64 {
        self.height * self.width
    }
}

// Here we say that T has the Shape type class (just like we do in Scala)
// which allows us to use all its operations
fn print_area<T: Shape> (shape: &T) {
    println!("{}", shape.area());
}

fn main() {
    let circle = Circle{radius: 2.0};
    let rectangle = Rectangle{height: 3.0, width: 5.0};

    print_area(&circle); // 12.5664
    print_area(&rectangle); // 15
}
