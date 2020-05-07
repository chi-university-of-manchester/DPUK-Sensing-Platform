//
//  ViewController.swift
//  DPUK Swift
//
//  Created by Dan on 08/03/2016.
//  Copyright © 2016 Dan. All rights reserved.
//

import UIKit
import CoreLocation

class ViewController: UIViewController, CLLocationManagerDelegate {
    
    private var locationManager = CLLocationManager()
    
    @IBOutlet weak var locationLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        locationManager.delegate = self
        locationManager.requestWhenInUseAuthorization()
    }
    
    @IBAction func locateButtonTouchUpInside(sender: AnyObject) {
        locationManager.requestLocation()
    }
    
    // MARK: CLLocationManagerDelegate
    func locationManager(manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        print(locations)
        if let latestLocation = locations.last {
            // latestlocation guaranteed to be set
            let payload = latestLocation.timestamp.description + ", " +
                latestLocation.coordinate.latitude.description + ", " +
                latestLocation.coordinate.longitude.description
            
            locationLabel.text = payload
            
            let JSONObject: [String : AnyObject] = [
                "payload" : payload,
                "startDate" : latestLocation.timestamp.description,
                "endDate" : latestLocation.timestamp.description,
                "dataSourceUserName" : "userName",
                "dataSourcePassword" : "password"
            ]
            
            print("JSONObject: ")
            print(JSONObject)
            
            if NSJSONSerialization.isValidJSONObject(JSONObject) {
                let request: NSMutableURLRequest = NSMutableURLRequest()
                
                //var err: NSError?
                
                request.URL = NSURL(string: "http://127.0.0.1")
                request.HTTPMethod = "POST"
                request.cachePolicy = NSURLRequestCachePolicy.ReloadIgnoringLocalCacheData
                request.setValue("application/json", forHTTPHeaderField: "Content-Type")
                request.HTTPBody = try! NSJSONSerialization.dataWithJSONObject(JSONObject, options:NSJSONWritingOptions(rawValue:0)/*, error:&err*/)
                
                let config = NSURLSessionConfiguration.defaultSessionConfiguration()
                let session = NSURLSession(configuration: config)
                let task = session.dataTaskWithRequest(request, completionHandler: {(data, response, error) in
                    print("Data: ")
                    print(data)
                    
                    print("Response: ")
                    print(response)
                    
                    print("Error: ")
                    print(error)
                });
                
                task.resume()
            }
        }
    }
    
    func locationManager(manager: CLLocationManager, didFailWithError error: NSError) {
        print(error)
    }
}