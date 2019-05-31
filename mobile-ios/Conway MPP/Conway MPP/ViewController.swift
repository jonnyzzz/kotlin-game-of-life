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

    @IBOutlet weak var text: UITextView!
    var world : WorldWrapper!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        world = WorldWrapper { [text] render -> KotlinUnit in
                text!.text = render
                return KotlinUnit()
        }
        GameKt.main()
        
        world?.doInitWorld()
    }

    @IBAction func onLongClick(_ sender: Any) {
          world?.doInitWorld()
    }
    
    @IBAction func onClick(_ sender: Any) {
         world?.iterateWorld()
    }
}

