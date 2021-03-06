# This is the user-interface definition of a Shiny web application.
# You can find out more about building applications with Shiny here:
#
# http://shiny.rstudio.com
#

library(shiny)

shinyUI(fluidPage(

  # Application title
  titlePanel("Car colors"),

  # Sidebar with a slider input for number of bins
  sidebarLayout(
    sidebarPanel(
      selectInput("variable", "Colors:",
                  c("Helsinki",
                    "Espoo",
                    "Vantaa",
                    "Oulu","Tampere","Kuopio")
                  )),

    # Show a plot of the generated distribution
    mainPanel(
      plotOutput("colorPlot")
    )
  )
))


