package com.qverkk.kotlin.framework.ui.trackers

import javafx.beans.property.IntegerProperty
import javafx.beans.property.StringProperty

class ItemTracker(var item: StringProperty,
                  var itemPrice: IntegerProperty,
                  var amount: IntegerProperty,
                  var amountHr: IntegerProperty,
                  var profit: IntegerProperty,
                  var profitHr: IntegerProperty)