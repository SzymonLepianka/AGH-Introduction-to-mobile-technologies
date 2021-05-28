// Szymon Lepianka wtorek 14:00
// laboratorium wykonane za pomocą http://online.swiftplayground.run/

import Foundation

// 1)
var a = "abcdef"
var b = 12
var c = 123.45

// 2)
let d: String = "sdfkljas"
let e: Int = 13
let f: Double = 425.6785

// 3)
let g1 = a + String(" ") + String(f)
let g2 = "\(a)\(" ")\(f)"

// 4)
var table = [1,2,3,4,5]
var dict = ["one": 1, "two": 2, "three": 3]

// 5)
table.append(6)
dict["four"] = 4

// 6)
var lotto = ["29-11-14": [4,5,21,30,31,49], "27-11-14": [5,8,10,19,23,40]]

// 7)
var emptyDictionary = [Character: Int]()

// 8)
for item in 1...10 {
    emptyDictionary[Character(UnicodeScalar(item+64)!)]=item
}
//print(emptyDictionary)

// 9)
for (k,v) in lotto {
    //print("Losowanie dnia " + k + ":")
    for item in v {
        //print("- " + String(item))
    } 
}

// 10)
func nwd(l1: Int, l2: Int) -> Int {   
    if l2 > 0 {
        return nwd(l1: l2, l2: l1%l2)
    } else {
        return l1
    }
}
// print(nwd(l1: 9,l2: 12))

// 11)
func nwd2(l1: Int, l2: Int) -> (Int, Int, Int) { 
    var dzielnik = 1
    for item in 1...min(l1, l2) {
        if (l1 % item == 0 && l2 % item == 0) {
            dzielnik = item
        }
    }
    return (dzielnik, l1/dzielnik, l2/dzielnik)
}
// print(nwd2(l1: 9,l2: 12))

// 12)
func func_pom(c: Character) -> Character {
    var changed: Character
    switch (c) {
    case "G":
       changed = "A"
    case "A":
        changed = "G"
    case "D":
        changed = "E"
    case "E":
        changed = "D"
    case "R":
        changed = "Y"
    case "Y":
        changed = "R"
    case "P":
        changed = "O"
    case "O":
        changed = "P"
    case "L":
        changed = "U"
    case "U":
        changed = "L"
    case "K":
        changed = "I"
    case "I":
        changed = "K"
    default:
        changed = c
    }
    return changed
}
func cipher(tekst: String, func_pom: (Character) -> Character) -> String {
    var zaszyfrowany = ""
    for c in tekst.uppercased() {
        zaszyfrowany += String(func_pom(c))
    }
    return zaszyfrowany
}
// print(cipher(tekst: "ASDfjhaAskjrsdrbvyc", func_pom: func_pom))

// 13)
for (date, tab) in lotto {
    let x = tab.map({ (number: Int) -> Int in
    if (number % 2 == 0){
        return 0
    }else{
        return 1
    }
    })   
    // print(String(date) + ": \(x)") 
}

// 14)
class NamedObject{
    var Name = "";
    
    func describe() -> String{
        return "Nazwa obiektu to: " + Name
    }

    // 15)
    class Sphere: NamedObject{
        var radius: Double;
        
        init(name: String, radius: Double){
            self.radius = radius
            super.init()
            self.Name = name       
        }
      
        var volume: Double {
            get {
                  return(4.0 / 3.0 * Double.pi * pow(radius, 3))
            }
            set {
                radius = pow(((3 * newValue) / (4 * Double.pi)), (1 / 3))
            }
        }
        
        override func describe() -> String {
            return "Nazwa obiektu to: " + Name + "\n parametry: \n  radius: " + String(radius) + "\n  volume: " + String(volume)
        }
    }
 }
var testowy1 = NamedObject();
testowy1.Name = "testowyObiekt";
print(testowy1.describe())

var testowy2 = NamedObject.Sphere(name: "Kula", radius: 5.7);
print(testowy2.describe())

testowy2.volume = 7.5
print(testowy2.describe())
