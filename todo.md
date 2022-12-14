
* **[GE-1]** Merge _abstractions_ `GameWord` and `Dispatcher`
   * **[GE-1.1]** objects should have direct access to the game to manipulate of common properties


* **[GE-2]** Add `GameWord` should be an `Obj` as well - to be part of the game loop and calculate some _common_ behaviour.


* **[GE-3]** Change the approach of `events` propagation
    * **motivation:** we need it to do not loop over all objects on each event
  * use classic `Listener` pattern
    * on `adding` of an `Obj` to game - automatically subscribe it to a number of events if it implements some interface. For instance: 
      * `PointerEventsListener` 
      * `TouchEventsListener`
      * `KeyboardEventsListener`
      * etc...

