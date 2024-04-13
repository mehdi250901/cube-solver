/* global describe, it, inject, expect, module, beforeEach, PivotTableTestUtils, ChartTestUtils */

describe('ChartContextualMenu', () => {
    let chartData, chartDef, chartStore, axisName1, axisName2, element, chartContextualMenu;
    let $rootScope;

    beforeEach(module('templates'));
    beforeEach(module('dataiku.charts', $provide => {
        $provide.value('ChartDataUtils', {});
    }));
    beforeEach(module('dataiku.shared'));
    beforeEach(module('dataiku.services'));
    beforeEach(module('dataiku.directives.widgets'));
    beforeEach(module('dataiku.mock'));
    beforeEach(module('dataiku.dashboards', $provide => {
        $provide.value('DashboardFilters', {
            canCrossFilter: () => true
        });
    }));

    beforeEach(ChartTestUtils.initAngularJSTemplatingResource);

    /* Initialize each test */
    beforeEach(() => {
        inject(function (ChartStoreFactory, ChartTensorDataWrapper, ChartContextualMenu) {
            $rootScope = ChartTestUtils.getScope();
            $('body').empty();
            element = angular.element(`
                <div class="chart-test" style="width: 15px; height: 15px; background-color: red;"></div>
            `);
            ChartTestUtils.renderElement(element, $rootScope);
            const { axesDef, chartDef, data } = PivotTableTestUtils.getPivotDataWith2RowDim1MeasureAggregAsRows();
            const axisNames = Object.keys(axesDef);
            axisName1 = axisNames[0];
            axisName2 = axisNames[1];
            chartData = new ChartTensorDataWrapper(data, axesDef);
            const { store, id } = ChartStoreFactory.getOrCreate(chartDef.$chartStoreId);
            chartDef.$chartStoreId = id;
            store.set('dimensionDict', new Map([[axisName1, chartDef.yDimension[0]], [axisName2, chartDef.yDimension[1]]]));
            chartStore = store;
            chartContextualMenu = ChartContextualMenu
    })});

    it('is defined', function () {
            expect(chartContextualMenu).toBeDefined();
    });

    it('should create a handler', function () {
            let contextualMenu = chartContextualMenu.create(chartData, chartDef, chartStore);
            expect(contextualMenu).toBeDefined();
    });

    it('should display a contextual menu on specific chart coords', function () {
            let contextualMenu = chartContextualMenu.create(chartData, chartDef, chartStore);
            const coords =  { [axisName1]: 1, [axisName2]: 1};
            contextualMenu.showForCoords(coords, new Event( element, { bubbles: true } ));
            $rootScope.$apply();
            expect(angular.element('[data-qa-charts-contextual-menu]').length).toBe(1);
            expect(angular.element('[data-qa-charts-contextual-menu-include-nd]').length).toBe(1);
            expect(angular.element('[data-qa-charts-contextual-menu-exclude-nd]').length).toBe(1);
            expect(angular.element('[data-qa-charts-contextual-menu-include-1d]').length).toBe(1);
            expect(angular.element('[data-qa-charts-contextual-menu-exclude-1d]').length).toBe(1);
    });
});
