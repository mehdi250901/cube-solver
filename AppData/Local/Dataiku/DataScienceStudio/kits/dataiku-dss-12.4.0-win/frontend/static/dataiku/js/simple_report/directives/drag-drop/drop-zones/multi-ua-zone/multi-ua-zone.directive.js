(function() {
    'use strict';

    const app = angular.module('dataiku.charts');

    // (!) This directive previously was in static/dataiku/js/simple_report/config_ui.js
    app.directive('multiUaZone', function($parse, ChartLabels) {
        return {
            templateUrl: '/static/dataiku/js/simple_report/directives/drag-drop/drop-zones/multi-ua-zone/multi-ua-zone.directive.html',
            scope: true,
            link: function($scope, element, attrs) {
                $scope.$watch(attrs.list, newList => $scope.list = newList);
                $scope.acceptCallback = $parse(attrs.acceptCallback)($scope);
                $scope.ChartLabels = ChartLabels;
            }
        }
    });
})();
