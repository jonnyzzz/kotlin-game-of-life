//
//  ViewController.swift
//  Conway MPP
//
//  Created by Evgeny Petrenko on 31.05.19.
//  Copyright © 2019 Evgeny Petrenko. All rights reserved.
//

import UIKit
import GameOfLifeFramework

class ViewController: UIViewController {

    @IBOutlet weak var text: UILabel!
    var world : WorldWrapper!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        GameKt.main()
        
        world = WorldWrapper { [text] render -> KotlinUnit in
            //TODO: how many chars fits into a line?
            var lineCount = 0
            var newText = ""
            
            for line in render.split(separator: "\n") {
                newText += line.prefix(12) + " \n"
                lineCount += 1
            }
            
            text!.numberOfLines = lineCount
            text!.text = newText
            
            return KotlinUnit()
        }
        world?.doInitWorld()
    }

    @IBAction func onLongClick(_ sender: Any) {
          world?.doInitWorld()
    }
    
    @IBAction func onClick(_ sender: Any) {
         world?.iterateWorld()
    }
}

