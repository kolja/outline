(ns outline.core
  (:require [quil.core :refer :all]
            [quil.middleware :as m]))

(defn setup []
   (frame-rate 70)
   {:boxes [        [3 3] [4 3]
              [2 4] [3 4] [4 4] [5 4]
              [2 5] [3 5] [4 5] [5 5] [6 5]
                    [3 6] [4 6] [5 6] [6 6]
                    [3 7] [4 7] [5 7]
                    [3 8] [4 8] [5 8] [6 8]]
    })

(defn update-state [state]
  state ;; just return the state unchanged for now
  )

(defn draw-boxes [boxes size space]
  (doseq [[x y] boxes]
    (rect (* (+ space size) x)
          (* (+ space size) y)
          size
          size)))

(defn draw [state]
  (background 79)
  (fill 142 200)
  (draw-boxes (:boxes state) 50 10))

(defsketch example
  :title "Example"
  :size [1000 1000]
  :setup setup
  :draw draw
  :update update-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])

