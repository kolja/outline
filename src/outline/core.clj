(ns outline.core
  (:require [quil.core :refer :all]
            [quil.middleware :as m]
            [midje.sweet :refer :all :exclude [background]]))

(comment
  (apply min [[2 4] [4 2]]))

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

(defn get-first-box [boxes]
  (first (sort-by (juxt second first) boxes)))

(def movement-map
  {:north [0 -1]
   :east [1 0]
   :south [0 1]
   :west [-1 0]})

(defn corners-from-box [[x y]]
  [[x y]
   [(inc x) y]
   [x (inc y)]
   [(inc x) (inc y)]])

(defn all-corners [boxes]
  (set (mapcat corners-from-box boxes)))

(fact ""
      (all-corners [[1 2] [2 2]]) => #{[1 2] [2 2] [3 2]
                                       [1 3] [2 3] [3 3]})

(defn move [from direction]
  (mapv + from (movement-map direction)))

(def rel {:north {:left :west
                  :straight :north
                  :right :east}
          :east {:left :north
                 :straight :east
                 :right :south}
          :south {:left :east
                  :straight :south
                  :right :west}
          :west {:left :south
                 :straight :west
                 :right :north}})

(defn outline [boxes]
  (let [initial (get-first-box boxes)
        corners (all-corners boxes)]
    (loop [last-direction :south
           result [initial]]
      (let [current-position (peek result)]
        (if-let [next-position (get corners (move (peek result) (-> rel last-direction :left)))]
          (recur (-> rel last-direction :left) (conj result next-position))
          (if-let [next-position (get corners (move (peek result) (-> rel last-direction :straight)))]
            (recur (-> rel last-direction :straight) (conj result next-position))
            (if-let [next-position (get corners (move (peek result) (-> rel last-direction :right)))]
              (recur (-> rel last-direction :right) (conj result next-position)))))))))

(fact ""
      (move [10 10] :up) => [10 9]
      (move [10 10] :right) => [11 10]
      (move [10 10] :down) => [10 11]
      (move [10 10] :left) => [9 10])

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

#_(defsketch example
    :title "Example"
    :size [1000 1000]
    :setup setup
    :draw draw
    :update update-state
    :features [:keep-on-top]
    :middleware [m/fun-mode])

