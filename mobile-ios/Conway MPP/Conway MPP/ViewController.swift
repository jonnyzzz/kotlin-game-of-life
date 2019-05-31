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

    @IBOutlet weak var myImage: UIImageView!
    
    var world = Maze_randomKt.randomMaze(width: 100, height: 100, p: 0.5)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        GameKt.main()
    }


    @IBAction func onClick(_ sender: Any) {
        world = world.nextGeneration(evolutionRule: { (<#EvolutionCell#>) -> CellState in
            <#code#>
        })
    }
}

