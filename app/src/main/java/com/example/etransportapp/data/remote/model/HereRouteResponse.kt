package com.example.etransportapp.data.remote.model

data class HereRouteResponse(
    val response: RouteResult
)

data class RouteResult(val route: List<Route>)

data class Route(
    val summary: Summary,
    val cost: Cost?
)

data class Summary(val distance: Int, val travelTime: Int)

data class TollCost(val value: Double, val currency: String)
data class Cost(
    val totalCost: String,
    val currency: String,
    val details: CostDetails
)

data class CostDetails(
    val tollCost: String,
    val driverCost: String,
    val vehicleCost: String
)
