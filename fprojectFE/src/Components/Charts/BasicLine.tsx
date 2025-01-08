// import { Line } from '@ant-design/plots';

// export function BasicLine() {
//     const dataChart = [
//         { date: "01-2023", quantity: 7455 },
//         { date: "02-2023", quantity: 6415 },
//         { date: "03-2023", quantity: 8276 },
//         { date: "04-2023", quantity: 7617 },
//         { date: "05-2023", quantity: 8380 },
//         { date: "06-2023", quantity: 9585 },
//         { date: "07-2023", quantity: 10620 },
//         { date: "08-2023", quantity: 11211 }
//     ];

//     const config = {
//         data: dataChart,
//         xField: 'date',
//         yField: 'quantity',
//         smooth: true, // Bật smooth để vẽ đường cong mượt mà
//         point: true, // Hiển thị các điểm trên đường cong
//         slider: {
//             start: 0,
//             end: 1,
//         },
//         lineStyle: {
//             lineWidth: 2, // Tùy chọn điều chỉnh độ rộng của đường
//         },
//         xAxis: {
//             type: 'time', // Đảm bảo trục x là kiểu thời gian
//             tickCount: 8, // Số lượng tick trên trục x
//         },
//     };

//     return (
//         <>
//             <h2>Basic Line</h2>
//             <Line {...config} />
//         </>
//     );
// }
