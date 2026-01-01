/**
 * ETHICA - Portfolio Chart
 * Uses balancesJson data passed from backend via Thymeleaf
 */
document.addEventListener('DOMContentLoaded', function () {
    var canvas = document.getElementById('myChart');

    // Exit if no canvas (empty state is shown instead)
    if (!canvas) return;

    // Check if Chart.js is loaded
    if (typeof Chart === 'undefined') {
        console.error('Chart.js not loaded');
        return;
    }

    // Check if we have data from backend
    if (typeof balancesJson === 'undefined' || !balancesJson) {
        console.log('No portfolio data available');
        return;
    }

    // Parse the JSON data from backend
    var chartData;
    try {
        chartData = typeof balancesJson === 'string' ? JSON.parse(balancesJson) : balancesJson;
    } catch (e) {
        console.error('Error parsing balancesJson:', e);
        return;
    }

    // Don't render chart if no data
    if (!chartData || chartData.length === 0) {
        console.log('Empty portfolio data');
        return;
    }

    // Extract labels (dates) and values (balances) from the data
    var labels = chartData.map(function(point) {
        // Format: "dd/MM/yyyy HH:mm:ss" -> show shorter version
        var dateParts = point.date.split(' ')[0]; // Get just "dd/MM/yyyy"
        var parts = dateParts.split('/');
        var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
        return parts[0] + ' ' + months[parseInt(parts[1], 10) - 1]; // "15 Jan"
    });

    var values = chartData.map(function(point) {
        return point.balance;
    });

    var ctx = canvas.getContext('2d');

    // Create gradient for fill
    var gradient = ctx.createLinearGradient(0, 0, 0, 300);
    gradient.addColorStop(0, 'rgba(16, 185, 129, 0.2)');
    gradient.addColorStop(1, 'rgba(16, 185, 129, 0.0)');

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Portfolio Value',
                data: values,
                borderColor: '#10b981',
                backgroundColor: gradient,
                borderWidth: 2,
                fill: true,
                tension: 0.4,
                pointRadius: 4,
                pointBackgroundColor: '#10b981',
                pointBorderColor: '#0a0a0b',
                pointBorderWidth: 2,
                pointHoverRadius: 8,
                pointHoverBackgroundColor: '#10b981',
                pointHoverBorderColor: '#ffffff',
                pointHoverBorderWidth: 3
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                mode: 'index',
                intersect: false
            },
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    enabled: true,
                    backgroundColor: 'rgba(17, 17, 19, 0.95)',
                    titleColor: '#fafafa',
                    bodyColor: '#10b981',
                    borderColor: 'rgba(16, 185, 129, 0.3)',
                    borderWidth: 1,
                    cornerRadius: 8,
                    padding: 12,
                    displayColors: false,
                    callbacks: {
                        title: function(context) {
                            // Show full date from original data
                            var index = context[0].dataIndex;
                            return chartData[index].date;
                        },
                        label: function(context) {
                            var value = context.parsed.y;
                            return '$' + value.toLocaleString('en-US', {
                                minimumFractionDigits: 2,
                                maximumFractionDigits: 2
                            });
                        }
                    }
                }
            },
            scales: {
                x: {
                    grid: {
                        display: false
                    },
                    ticks: {
                        color: 'rgba(255, 255, 255, 0.4)',
                        maxRotation: 45,
                        minRotation: 0,
                        // Show fewer labels if too many data points
                        maxTicksLimit: 12
                    }
                },
                y: {
                    grid: {
                        color: 'rgba(255, 255, 255, 0.06)'
                    },
                    ticks: {
                        color: 'rgba(255, 255, 255, 0.4)',
                        callback: function(value) {
                            if (value >= 1000000) {
                                return '$' + (value / 1000000).toFixed(1) + 'M';
                            } else if (value >= 1000) {
                                return '$' + (value / 1000).toFixed(0) + 'k';
                            }
                            return '$' + value.toFixed(0);
                        }
                    },
                    // Start from 0 or slightly below min value
                    beginAtZero: false
                }
            }
        }
    });
});