<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Dashboard: Doanh thu &amp; Hàng tồn kho</title>
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <style>
  body {
    font-family: Arial, sans-serif;
    padding: 20px;
  }
  h1 {
    text-align: center;
    margin-bottom: 40px;
  }
  .chart-container {
    width: 48%;
    display: inline-block;
    vertical-align: top;
    box-sizing: border-box;
  }
  /* Chỉ cố định chiều cao cho inventory chart */
  #inventoryChart {
    /* chiều cao mong muốn */
    height: 400px !important;
  }
  #revenueChart {
    /* chiều cao mong muốn */
    height: 400px !important;
  }
  /* Canvas ở trong container mặc định tự động co rộng, chỉ cần override height */
  .chart-container canvas {
    width: 100% !important;
  }
</style>
</head>
<body>
  <h1>Quản lý Doanh thu &amp; Hàng tồn kho</h1>

  <div class="chart-container">
    <h2>Doanh thu theo tháng</h2>
    <canvas id="revenueChart"></canvas>
  </div>

  <div class="chart-container">
    <h2>Tồn kho theo sản phẩm</h2>
    <canvas id="inventoryChart"></canvas>
  </div>

  <script>
    // === Data Doanh thu ===
    const revLabels = [], revData = [];
    <c:forEach var="e" items="${revenues}">
      revLabels.push('${e.key}');
      revData.push(${e.value});
    </c:forEach>

    new Chart(
      document.getElementById('revenueChart').getContext('2d'),
      {
        type: 'bar',
        data: {
          labels: revLabels,
          datasets: [{
            label: 'Doanh thu (VND)',
            data: revData,
            backgroundColor: 'rgba(54, 162, 235, 0.7)',
            borderColor:   'rgba(54, 162, 235, 1)',
            borderWidth: 1
          }]
        },
        options: {
          maintainAspectRatio: false,
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                callback: value => new Intl.NumberFormat('vi-VN').format(value)
              }
            }
          },
          plugins: {
            tooltip: {
              callbacks: {
                label: function(ctx) {
                  return new Intl.NumberFormat('vi-VN', {
                    style: 'currency',
                    currency: 'VND'
                  }).format(ctx.parsed.y);
                }
              }
            }
          }
        }
      }
    );

    // === Data Tồn kho ===
    const invLabels = [], invData = [];
    <c:forEach var="inv" items="${inventories}">
      invLabels.push('${inv.key}');
      invData.push(${inv.value});
    </c:forEach>

    const invColors = invData.map(v => {
      if (v < 50)   return 'rgba(255, 99, 132, 0.7)';
      if (v < 150)  return 'rgba(255, 206, 86, 0.7)';
                     return 'rgba(75, 192, 192, 0.7)';
    });
    const invBorders = invColors.map(c => c.replace(/0\.7\)$/, '1)'));

    new Chart(
      document.getElementById('inventoryChart').getContext('2d'),
      {
        type: 'bar',
        data: {
          labels: invLabels,
          datasets: [{
            label: 'Số lượng tồn kho',
            data: invData,
            backgroundColor: invColors,
            borderColor: invBorders,
            borderWidth: 1
          }]
        },
        options: {
          indexAxis: 'y',
          maintainAspectRatio: false,
          scales: {
            x: {
              beginAtZero: true,
              ticks: {
                callback: v => Number(v).toLocaleString('vi-VN')
              }
            },
            y: { ticks: { autoSkip: false } }
          },
          plugins: {
            legend: { display: false },
            tooltip: {
              callbacks: {
                label: function(ctx) {
                  return ctx.label + ': ' +
                    Number(ctx.parsed.x).toLocaleString('vi-VN') + ' pcs';
                }
              }
            }
          }
        }
      }
    );
  </script>
</body>
</html>
