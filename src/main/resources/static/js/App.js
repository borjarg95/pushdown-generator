// Define the module
var pgApp = angular.module('pgApp', []);

// Define the `AutomatorController` controller on the `pgApp` module
pgApp.controller('AutomatonController', function AutomatonController($scope) {
  $scope.examples = [
    {
        name: "Automata no determinista",
        definition: EXAMPLE_NOT_DETERMINISTIC
    },
    {
        name: "Automata simple determinista",
        definition: EXAMPLE_SIMPLE_DETERMINISTIC
    }
  ];
  // TODO: allows to be compatible with native JS code
  // need to remove this before setup angular
  Main();
});